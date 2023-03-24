package com.billdiary.controller;


import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ApiConstants;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dto.ProductDto;
import com.billdiary.exception.DatabaseException;
import com.billdiary.exception.DatabaseRuntimeException;
import com.billdiary.dto.RestResponse;
import com.billdiary.service.ProductService;
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

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @Autowired
    MessageConfig messageConfig;

    @ApiOperation(value = "Get list of Products in the System ", response = RestResponse.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping
    public ResponseEntity<RestResponse> getProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "5") int size){
         try{
            logger.debug("Getting Products");
            RestResponse response = new RestResponse();
            response.setData(productService.getProducts(page, size));
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
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get Product in the System by Id", response = RestResponse.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{productId}")
    public ResponseEntity<RestResponse> getProductById(@PathVariable("productId") Long productId){
        logger.debug("Getting product productId {}", productId);
        try{
            RestResponse response = new RestResponse();
            response.setData(productService.getProductById(productId));
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

    @ApiOperation(value = "Create Product in the System", response = RestResponse.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping
    public ResponseEntity<RestResponse> createProducts(@RequestBody List<ProductDto> productDtos){
        try{
            RestResponse response = new RestResponse();
            response.setData(productService.saveProducts(productDtos));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (DatabaseRuntimeException e) {
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

    @ApiOperation(value = "Update Product in the System", response = RestResponse.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public ResponseEntity<RestResponse> updateProduct(ProductDto productDto){
        try{
            RestResponse response = new RestResponse();
            response.setData(productService.updateProduct(productDto));
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
