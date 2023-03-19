package com.billdiary.controller;

import com.billdiary.entity.Customer;
import com.billdiary.model.ErrorResponse;
import com.billdiary.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
@RestController
@RequestMapping("/customer")
public class CustomerController {


    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    @ApiOperation(value = "Get list of Customers in the System ", response = Iterable.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getCustomers();
    }

    @ApiOperation(value = "Get Customer in the System by Id", response = Customer.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId){
        logger.debug("Getting Customer customerId {}", customerId);
        try{
            return new ResponseEntity(customerService.getCustomerById(customerId), HttpStatus.OK);

        }catch(NoSuchElementException ex){
            logger.error(ex.getMessage(),ex);
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Create Customer in the System", response = Customer.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping
    public ResponseEntity<Customer> createCustomer(Customer customer){
        List<Customer> createdCustomer = customerService.saveCustomers(new ArrayList<>(Arrays.asList(customer)));
        if (null != createdCustomer && createdCustomer.size() != 0){
            return new ResponseEntity(createdCustomer, HttpStatus.OK);
        }
        return new ResponseEntity(new ErrorResponse("100","Customer Id or Name already exits"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Update Customer in the System", response = Customer.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public ResponseEntity<Customer> updateCustomer(Customer Customer){
        Customer Customer1 = customerService.updateCustomer(Customer);
        if (null != Customer1){
            return new ResponseEntity(Customer1, HttpStatus.OK);
        }else{
            return new ResponseEntity(new ErrorResponse("101","Customer does not exits or Your are assigning Customer code which already exists for other Customer"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete Customer in the System", response = Customer.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping
    public ResponseEntity<Customer> deleteCustomer(Customer Customer){
        boolean deletedCustomer = customerService.deleteCustomer(Customer.getCustomerId());
        if (deletedCustomer){
            return new ResponseEntity(deletedCustomer, HttpStatus.OK);
        }else{
            return new ResponseEntity(new ErrorResponse("101","Customer does not exits or Your are assigning Customer code which already exists for other Customer"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
