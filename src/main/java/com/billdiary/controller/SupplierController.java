package com.billdiary.controller;


import com.billdiary.entity.Product;
import com.billdiary.entity.Supplier;
import com.billdiary.model.ErrorResponse;
import com.billdiary.service.SupplierService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    SupplierService supplierService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping
    public List<Supplier> getSuppliers(){
        return supplierService.getSuppliers();
    }


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{supplierId}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable("supplierId") Long supplierId){
        logger.debug("Getting supplier supplierId {}", supplierId);
        try{
            return new ResponseEntity(supplierService.getSupplierById(supplierId), HttpStatus.OK);

        }catch(NoSuchElementException ex){
            logger.error(ex.getMessage(),ex);
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping
    public ResponseEntity<Supplier> createProduct(Supplier supplier){
        Supplier createdSupplier = supplierService.createSupplier(supplier);
        if (null != createdSupplier){
            return new ResponseEntity(createdSupplier, HttpStatus.OK);
        }
        return new ResponseEntity(new ErrorResponse("100","Supplier Id or Name already exits"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public ResponseEntity<Supplier> updateProduct(Supplier supplier){

        Supplier supplier1 = supplierService.updateSupplier(supplier);
        if (null != supplier1){
            return new ResponseEntity(supplier1, HttpStatus.OK);
        }else{
            return new ResponseEntity(new ErrorResponse("101","Supplier not exits"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
