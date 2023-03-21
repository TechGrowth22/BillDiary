package com.billdiary.controller;


import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ApiConstants;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.exception.DatabaseException;
import com.billdiary.model.RestResponse;
import com.billdiary.service.ProductCategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    MessageConfig messageConfig;

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

}
