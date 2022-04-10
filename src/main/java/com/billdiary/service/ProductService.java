package com.billdiary.service;

import com.billdiary.controller.ProductController;
import com.billdiary.dao.ProductRepository;
import com.billdiary.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts() {
        logger.debug("Getting all the products from repository");
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        logger.debug("Getting product from repository productId {}", productId);
        return productRepository.findById(productId).get();
    }

    public Product createProduct(Product product) {
        logger.debug("Creating a product {}", product.toString());
        return productRepository.saveAndFlush(product);
    }

    public Product updateProduct(Product product) {
        logger.debug("Updating the product {}", product.toString());
        return productRepository.saveAndFlush(product);
    }
}
