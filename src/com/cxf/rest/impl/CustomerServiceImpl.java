package com.cxf.rest.impl;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.cxf.rest.CustomerService;
import com.cxf.rest.Order;
import com.cxf.rest.dto.Customer;

@Path("/customerservice")
// @Produces("application/json,application/xml")
// @Consumes("application/json,application/xml")
// @Produces("application/xml")
// @Consumes("application/xml")
public class CustomerServiceImpl implements CustomerService {
	String currentId = "123";
	Map<String, Customer> customers = new HashMap<String, Customer>();
	Map<Long, Order> orders = new HashMap<Long, Order>();

	public CustomerServiceImpl() {
		init();
	}

	@GET
	@Path("/customers/get/{id}/{userName}")
	@Produces("application/xml")
	@Override
	public Customer getCustomer(@PathParam("id") String id,
			@PathParam("userName") String userName) {
		System.out.println("----invoking getCustomer, Customer id is: " + id);
		String idNumber = id;
		// int i = 1 / 0;
		Customer c = customers.get(idNumber);
		return c;
	}

	@PUT
	@Path("/customers/update/{id}")
	@Override
	public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
		System.out.println("----invoking updateCustomer, Customer name is: "
				+ customer.getName());
		Customer c = customers.get(customer.getId());
		if (c == null || c.getId() != customer.getId()) {
			throw new WebApplicationException();
		}
		Response r = null;
		if (c != null) {
			customers.put(customer.getId(), customer);
			r = Response.ok(customer).build();
			// r = Response.notModified().build();
		}
		return r;
	}

	@POST
	@Path("/customers/add")
	@Consumes("application/xml")
	@Override
	public Response addCustomer(Customer customer) {
		System.out.println("----invoking addCustomer, Customer name is: "
				+ customer.getName());
		int i = Integer.parseInt(currentId);
		i = i + 1;
		currentId = String.valueOf(i);
		customer.setId(currentId);

		customers.put(customer.getId(), customer);

		return Response.ok(customer).build();
	}

	@DELETE
	@Path("/customers/delete/{id}")
	@Override
	public Response deleteCustomer(@PathParam("id") String id) {
		System.out
				.println("----invoking deleteCustomer, Customer id is: " + id);
		long idNumber = Long.parseLong(id);
		Customer c = customers.remove(idNumber);

		Response r;
		if (c != null) {
			r = Response.ok().build();
		} else {
			r = Response.notModified().build();
		}

		return r;
	}

	final void init() {
		Customer c = new Customer();
		c.setName("John");
		c.setId("123");
		customers.put(c.getId(), c);

		Order o = new Order();
		o.setDescription("order 223");
		o.setId(223);
		orders.put(o.getId(), o);
	}

}
