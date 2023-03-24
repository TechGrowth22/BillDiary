package com.billdiary.controller;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ApiConstants;
import com.billdiary.controller.utility.ResponseUtility;
import com.billdiary.dto.ProductCategoryDto;
import com.billdiary.dto.RestResponse;
import com.billdiary.service.ProductCategoryService;
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
@RequestMapping("/product-category")
public class ProductCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    MessageConfig messageConfig;

    @Autowired
    ResponseUtility responseUtility;

    @Autowired
    ProductCategoryService productCategoryService;

    @ApiOperation(value = "Get list of Product Categories in the System ", response = RestResponse.class, tags = "product-category-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping
    public ResponseEntity<RestResponse> getProductCategories(@RequestParam(name = "page", defaultValue = "0") int page,
                                                    @RequestParam(name = "size", defaultValue = "5") int size){
        try{
            logger.debug("Getting Products");
            RestResponse response = new RestResponse();
            response.setData(productCategoryService.getProductCategories(page, size));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

    @ApiOperation(value = "Get ProductCategory in the System by Id", response = RestResponse.class, tags = "product-category-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{productCategoryId}")
    public ResponseEntity<RestResponse> getProductCategoryById(@PathVariable("productCategoryId") Long productCategoryId){
        logger.debug("Getting productCategory productCategoryId {}", productCategoryId);
        try{
            RestResponse response = new RestResponse();
            response.setData(productCategoryService.getProductCategoryById(productCategoryId));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

    @ApiOperation(value = "Create Product Category in the System", response = RestResponse.class, tags = "product-category-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping
    public ResponseEntity<RestResponse> createProductCategories(@RequestBody List<ProductCategoryDto> productCategorieDtos){
        try{
            RestResponse response = new RestResponse();
            response.setData(productCategoryService.saveProductCategories(productCategorieDtos));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

    @ApiOperation(value = "Update Product Category in the System", response = RestResponse.class, tags = "product-category-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public ResponseEntity<RestResponse> updateProductCategory(ProductCategoryDto productCategoryDto){
        try{
            RestResponse response = new RestResponse();
            response.setData(productCategoryService.updateProductCategory(productCategoryDto));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

    @ApiOperation(value = "Delete Product Category in the System", response = RestResponse.class, tags = "product-category-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping("/{productCategoryId}")
    public ResponseEntity<ProductCategoryDto> deleteProductCategory(@PathVariable("productCategoryId") Long productCategoryId){
        try{
            RestResponse response = new RestResponse();
            response.setData(productCategoryService.deleteProductCategory(productCategoryId));
            response.setStatus(ApiConstants.STATUS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return responseUtility.handleExceptionResponse(e);
        }
    }

}
