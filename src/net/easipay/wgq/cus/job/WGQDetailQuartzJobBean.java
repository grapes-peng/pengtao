package net.easipay.wgq.cus.job;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class WGQDetailQuartzJobBean extends QuartzJobBean {
	protected final Logger logger = Logger.getLogger(getClass());
	private String targetObject;
	private String targetMethod;
	private ApplicationContext ctx;

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			Object otargetObject = ctx.getBean(targetObject);
			synchronized (otargetObject) {
				logger.info("execute ["
						+ targetObject
						+ "] at once>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				logger.info("otargetObject [" + otargetObject + "]");
				Method m = null;
				try {
					m = otargetObject.getClass().getMethod(targetMethod,
							new Class[] {});
					logger.info("getMethod [" + m + "]");
					m.invoke(otargetObject, new Object[] {});
					logger.info("after invoke execute.....");
				} catch (SecurityException e) {
					logger.error(e.getMessage(), e);
					throw new JobExecutionException(e);
				} catch (NoSuchMethodException e) {
					logger.error(e.getMessage(), e);
					throw new JobExecutionException(e);
				}
				logger.info("execute [" + targetObject + "] finish>>>>>>");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JobExecutionException(e);
		}
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public ApplicationContext getCtx() {
		return ctx;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {

		this.ctx = applicationContext;

	}

}