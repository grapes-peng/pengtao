package org.apache.cxf.jaxrs.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.JAXRSServiceImpl;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.jaxrs.impl.RequestPreprocessor;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.model.ProviderInfo;
import org.apache.cxf.jaxrs.provider.ProviderFactory;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.jaxrs.utils.InjectionUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.service.Service;

public class JAXRSInInterceptor extends
		AbstractPhaseInterceptor<org.apache.cxf.message.Message> {
	private static final Logger LOG = LogUtils
			.getL7dLogger(JAXRSInInterceptor.class);
	private static final ResourceBundle BUNDLE = BundleUtils
			.getBundle(JAXRSInInterceptor.class);
	private static final String RESOURCE_METHOD = "org.apache.cxf.resource.method";
	private static final String RESOURCE_OPERATION_NAME = "org.apache.cxf.resource.operation.name";

	public JAXRSInInterceptor() {
		super("unmarshal");
	}

	public void handleFault(org.apache.cxf.message.Message message) {
		super.handleFault(message);

		LOG.fine("Cleanup thread local variables");

		Object rootInstance = message.getExchange().remove(
				"service.root.instance");
		Object rootProvider = message.getExchange().remove(
				"service.root.provider");
		if ((rootInstance != null) && (rootProvider != null)) {
			try {
				((ResourceProvider) rootProvider).releaseInstance(message,
						rootInstance);
			} catch (Throwable tex) {
				LOG.warning("Exception occurred during releasing the service instance, "
						+ tex.getMessage());
			}
		}
		ProviderFactory.getInstance(message).clearThreadLocalProxies();
		ClassResourceInfo cri = (ClassResourceInfo) message.getExchange().get(
				"root.resource.class");
		if (cri != null)
			cri.clearThreadLocalProxies();
	}

	public void handleMessage(org.apache.cxf.message.Message message) {
		if (message.getExchange().get(OperationResourceInfo.class) != null) {
			return;
		}
		message.getExchange().put("org.apache.cxf.rest.message", Boolean.TRUE);
		try {
			processRequest(message);
		} catch (Fault ex) {
			convertExceptionToResponseIfPossible(ex.getCause(), message);
		} catch (RuntimeException ex) {
			convertExceptionToResponseIfPossible(ex, message);
		}
	}

	private void processRequest(org.apache.cxf.message.Message message) {
		ProviderFactory providerFactory = ProviderFactory.getInstance(message);

		RequestPreprocessor rp = providerFactory.getRequestPreprocessor();
		if (rp != null) {
			rp.preprocess(message, new UriInfoImpl(message, null));
			if (message.getExchange().get(Response.class) != null) {
				return;
			}

		}

		if (JAXRSUtils.runContainerRequestFilters(providerFactory, message,
				true, null)) {
			return;
		}
		String httpMethod = HttpUtils.getProtocolHeader(message,
				"org.apache.cxf.request.method", "POST", true);

		String requestContentType = (String) message.get("Content-Type");
		if (requestContentType == null) {
			requestContentType = "*/*";
		}

		String rawPath = HttpUtils.getPathToMatch(message, true);

		Service service = (Service) message.getExchange().get(Service.class);
		List<ClassResourceInfo> resources = ((JAXRSServiceImpl) service).getClassResourceInfos();

		String acceptTypes = HttpUtils.getProtocolHeader(message, "Accept",
				null);
		if (acceptTypes == null) {
			acceptTypes = "*/*";
			message.put("Accept", acceptTypes);
		}
		List<MediaType> acceptContentTypes = null;
		try {
			acceptContentTypes = JAXRSUtils.sortMediaTypes(acceptTypes, "q");
		} catch (IllegalArgumentException ex) {
			throw new NotAcceptableException();
		}
		message.getExchange().put("Accept", acceptContentTypes);

		MultivaluedMap values = new MetadataMap();
		ClassResourceInfo resource = JAXRSUtils.selectResourceClass(resources,
				rawPath, values, message);

		if (resource == null) {
			org.apache.cxf.common.i18n.Message errorMsg = new org.apache.cxf.common.i18n.Message(
					"NO_ROOT_EXC", BUNDLE,
					new Object[] { message.get("org.apache.cxf.request.uri"),
							rawPath });

			LOG.warning(errorMsg.toString());
			Response resp = JAXRSUtils.createResponse(null, message,
					errorMsg.toString(),
					Response.Status.NOT_FOUND.getStatusCode(), false);

			throw new NotFoundException(resp);
		}

		message.getExchange().put("root.resource.class", resource);

		OperationResourceInfo ori = null;

		boolean operChecked = false;
		List<ProviderInfo<RequestHandler>> shs = providerFactory
				.getRequestHandlers();
		for (ProviderInfo sh : shs) {
			if ((ori == null) && (!operChecked)) {
				try {
					ori = JAXRSUtils.findTargetMethod(resource, message,
							httpMethod, values, requestContentType,
							acceptContentTypes, false);

					setExchangeProperties(message, ori, values,
							resources.size());
				} catch (WebApplicationException ex) {
					operChecked = true;
				}
			}

			InjectionUtils.injectContexts(sh.getProvider(), sh, message);
			Response response = ((RequestHandler) sh.getProvider())
					.handleRequest(message, resource);
			if (response != null) {
				message.getExchange().put(Response.class, response);
				return;
			}

		}

		if (ori == null) {
			try {
				ori = JAXRSUtils.findTargetMethod(resource, message,
						httpMethod, values, requestContentType,
						acceptContentTypes, true);

				setExchangeProperties(message, ori, values, resources.size());
			} catch (WebApplicationException ex) {
				if (JAXRSUtils.noResourceMethodForOptions(ex.getResponse(),
						httpMethod)) {
					Response response = JAXRSUtils.createResponse(resource,
							null, null, 200, true);
					message.getExchange().put(Response.class, response);
					return;
				}
				throw ex;
			}

		}

		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Request path is: " + rawPath);
			LOG.fine("Request HTTP method is: " + httpMethod);
			LOG.fine("Request contentType is: " + requestContentType);
			LOG.fine("Accept contentType is: " + acceptTypes);

			LOG.fine("Found operation: " + ori.getMethodToInvoke().getName());
		}

		setExchangeProperties(message, ori, values, resources.size());

		if (JAXRSUtils.runContainerRequestFilters(providerFactory, message,
				false, ori.getNameBindings())) {
			return;
		}

		try {
			List params = JAXRSUtils.processParameters(ori, values, message);
			message.setContent(List.class, params);
		} catch (IOException ex) {
			convertExceptionToResponseIfPossible(ex, message);
		}
	}

	private void convertExceptionToResponseIfPossible(Throwable ex,
			org.apache.cxf.message.Message message) {
		Response excResponse = JAXRSUtils.convertFaultToResponse(ex, message);
		if (excResponse == null) {
			ProviderFactory.getInstance(message).clearThreadLocalProxies();
			message.getExchange().put(
					org.apache.cxf.message.Message.PROPOGATE_EXCEPTION,
					Boolean.valueOf(JAXRSUtils.propogateException(message)));

			throw ((ex instanceof RuntimeException) ? (RuntimeException) ex
					: new InternalServerErrorException(ex));
		}
		message.getExchange().put(Response.class, excResponse);
	}

	private void setExchangeProperties(org.apache.cxf.message.Message message,
			OperationResourceInfo ori, MultivaluedMap<String, String> values,
			int numberOfResources) {
		message.getExchange().put(OperationResourceInfo.class, ori);
		message.put("org.apache.cxf.resource.method", ori.getMethodToInvoke());
		message.put("jaxrs.template.parameters", values);

		String plainOperationName = ori.getMethodToInvoke().getName();
		if (numberOfResources > 1) {
			plainOperationName = ori.getClassResourceInfo().getServiceClass()
					.getSimpleName()
					+ "#" + plainOperationName;
		}

		message.getExchange().put("org.apache.cxf.resource.operation.name",
				plainOperationName);

		boolean oneway = (ori.isOneway())
				|| (MessageUtils.isTrue(HttpUtils.getProtocolHeader(message,
						"OnewayRequest", null)));

		message.getExchange().setOneWay(oneway);
	}
}