package com.billdiary.controller;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ApiConstants;
import com.billdiary.controller.utility.ResponseUtility;
import com.billdiary.dto.ProductDto;
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
    ResponseUtility responseUtility;

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

        }catch(Exception e){
             logger.error(e.getMessage(), e);
             return responseUtility.handleExceptionResponse(e);
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

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
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
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
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

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

}
