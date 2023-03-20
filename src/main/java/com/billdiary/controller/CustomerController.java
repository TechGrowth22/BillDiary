package com.billdiary.controller;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ApiConstants;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.entity.Customer;
import com.billdiary.exception.DatabaseException;
import com.billdiary.model.RestResponse;
import com.billdiary.service.CustomerService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    @Autowired
    MessageConfig messageConfig;

    @ApiOperation(value = "Get list of Customers in the System ", response = RestResponse.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping
    public ResponseEntity<RestResponse> getCustomers(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "5") int size){
        try{
            logger.debug("Getting All Customers");
            RestResponse response = new RestResponse();
            response.setData(customerService.getCustomers(page, size));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            RestResponse response = new RestResponse(ErrorConstants.Err_Code_101, messageConfig.getMessage(ErrorConstants.Err_Code_101));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get Customer in the System by Id", response = RestResponse.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId){
        logger.debug("Getting Customer customerId {}", customerId);
        try{
            RestResponse response = new RestResponse();
            response.setData(customerService.getCustomerById(customerId));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch (DatabaseException e) {
            logger.error(e.getMessage(),e);
            RestResponse response = new RestResponse();
            response.setStatus(ApiConstants.STATUS_FAILED);
            response.setErrorCode(e.getErrorCode());
            response.setErrorMessage(e.getErrorMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            RestResponse response = new RestResponse(ErrorConstants.Err_Code_101, messageConfig.getMessage(ErrorConstants.Err_Code_101));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Create Customer in the System", response = RestResponse.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping
    public ResponseEntity<RestResponse> createCustomer(@RequestBody List<Customer> customers){
        try{

            RestResponse response = new RestResponse();
            response.setData(customerService.saveCustomers(customers));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            RestResponse response = new RestResponse(ErrorConstants.Err_Code_101, messageConfig.getMessage(ErrorConstants.Err_Code_101));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update Customer in the System", response = RestResponse.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer Customer){
        try {
            RestResponse response = new RestResponse();
            response.setData(customerService.updateCustomer(Customer));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch (DatabaseException e) {
            logger.error(e.getMessage(),e);
            RestResponse response = new RestResponse();
            response.setStatus(ApiConstants.STATUS_FAILED);
            response.setErrorCode(e.getErrorCode());
            response.setErrorMessage(e.getErrorMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            RestResponse response = new RestResponse(ErrorConstants.Err_Code_101, messageConfig.getMessage(ErrorConstants.Err_Code_101));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete Customer in the System", response = RestResponse.class, tags = "customer-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("customerId") Long customerId){
        try{
            RestResponse response = new RestResponse();
            response.setData(customerService.deleteCustomer(customerId));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (DatabaseException e) {
            logger.error(e.getMessage(),e);
            RestResponse response = new RestResponse();
            response.setStatus(ApiConstants.STATUS_FAILED);
            response.setErrorCode(e.getErrorCode());
            response.setErrorMessage(e.getErrorMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            RestResponse response = new RestResponse(ErrorConstants.Err_Code_101, messageConfig.getMessage(ErrorConstants.Err_Code_101));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
