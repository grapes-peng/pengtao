package com.cxf.rest;

<<<<<<< HEAD
=======
import javax.jws.WebService;
>>>>>>> origin/master
import javax.ws.rs.core.Response;

import com.cxf.rest.dto.Customer;

<<<<<<< HEAD
public interface CustomerService {

	Customer getCustomer(String id, String userName);

	Response updateCustomer(Long id, Customer customer);

	Response addCustomer(Customer customer);

	Response deleteCustomer(String id);
=======
@WebService
public interface CustomerService {

	Response addCustomer(Customer customer);

	Customer getCustomer(String id, String userName);
>>>>>>> origin/master

}
