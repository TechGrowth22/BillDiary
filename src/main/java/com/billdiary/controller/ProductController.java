package com.billdiary.controller;


import com.billdiary.entity.Product;
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
    public List<Product> getProduct(){
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
    public Product createProduct(Product product){
        return productService.createProduct(product);
    }

    @ApiOperation(value = "Update Product in the System", response = Product.class, tags = "product-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping
    public Product updateProduct(Product product){
        return productService.updateProduct(product);
    }
}
