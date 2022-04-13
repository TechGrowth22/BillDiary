package com.billdiary.controller;


import com.billdiary.entity.Product;
import com.billdiary.entity.Supplier;
import com.billdiary.entity.Unit;
import com.billdiary.model.ErrorResponse;
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
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @ApiOperation(value = "Get list of Products in the System ", response = Iterable.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @ApiOperation(value = "Get Product in the System by Id", response = Product.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId){
        logger.debug("Getting product productId {}", productId);
        try{
            return new ResponseEntity(productService.getProductById(productId), HttpStatus.OK);

        }catch(NoSuchElementException ex){
            logger.error(ex.getMessage(),ex);
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Create Product in the System", response = Product.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping
    public ResponseEntity<Product> createProduct(Product product){
        Product createdProduct = productService.createProduct(product);
        if (null != createdProduct){
            return new ResponseEntity(createdProduct, HttpStatus.OK);
        }
        return new ResponseEntity(new ErrorResponse("100","Product Id or Name already exits"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Update Product in the System", response = Product.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public ResponseEntity<Product> updateProduct(Product product){
        Product product1 = productService.updateProduct(product);
        if (null != product1){
            return new ResponseEntity(product1, HttpStatus.OK);
        }else{
            return new ResponseEntity(new ErrorResponse("101","Product does not exits or Your are assigning product code which already exists for other product"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get all units in the System", response = Unit.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/unit")
    public List<Unit> getAllUnits(){
        return productService.getAllUnits();
    }


}
