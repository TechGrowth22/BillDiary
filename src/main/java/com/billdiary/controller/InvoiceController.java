package com.billdiary.controller;

import com.billdiary.constant.ApiConstants;
import com.billdiary.controller.utility.ResponseUtility;
import com.billdiary.entity.Invoice;
import com.billdiary.exception.DatabaseException;
import com.billdiary.dto.RestResponse;
import com.billdiary.service.InvoiceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ResponseUtility responseUtility;

    @ApiOperation(value = "Get list of Invoices in the System ", response = RestResponse.class, tags = "invoice-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping
    public ResponseEntity<RestResponse> getInvoices(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "5") int size){
        try{
            logger.debug("Getting All Invoices");
            RestResponse response = new RestResponse();
            response.setData(invoiceService.getInvoices(page, size));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

    @ApiOperation(value = "Get Invoice in the System by Id", response = RestResponse.class, tags = "invoice-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable("invoiceId") Long invoiceId){
        logger.debug("Getting Invoice invoiceId {}", invoiceId);
        try{
            RestResponse response = new RestResponse();
            response.setData(invoiceService.getInvoiceById(invoiceId));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

    @ApiOperation(value = "Create Invoice in the System", response = RestResponse.class, tags = "invoice-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping
    public ResponseEntity<RestResponse> createInvoice(@RequestBody Invoice invoice){
        try{

            RestResponse response = new RestResponse();
            response.setData(invoiceService.saveInvoices(invoice));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }



    @ApiOperation(value = "Update Invoice in the System", response = RestResponse.class, tags = "invoice-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice Invoice){
        try {
            RestResponse response = new RestResponse();
            response.setData(invoiceService.updateInvoice(Invoice));
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
            return responseUtility.handleExceptionResponse(e);
        }
    }

    @ApiOperation(value = "Delete Invoice in the System", response = RestResponse.class, tags = "invoice-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("invoiceId") Long invoiceId){
        try{
            RestResponse response = new RestResponse();
            response.setData(invoiceService.deleteInvoice(invoiceId));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

}
