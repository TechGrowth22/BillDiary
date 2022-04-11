package com.billdiary.service;

import com.billdiary.controller.ProductController;
import com.billdiary.dao.ProductRepository;
import com.billdiary.dao.UnitRepository;
import com.billdiary.entity.Product;
import com.billdiary.entity.Unit;
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

    @Autowired
    UnitRepository unitRepository;

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
        /**
         * Validating Unit
         */
        Unit unit = product.getUnit();
        if (null!=unit){
            List<Unit> units = unitRepository.findByUnitNameIgnoreCase(unit.getUnitName());
            if (null != units && units.size()>0)
                unit.setUnitId(units.get(0).getUnitId());
            else
                unit.setUnitId(0l);
        }


        List<Product> products = productRepository.findByproductCodeOrProductName(product.getProductCode(),product.getProductName());
        Product matchingObject = products.stream().
                filter(p -> p.getProductCode().equals(product.getProductCode()) || p.getProductName().equals(product.getProductName())).
                findAny().orElse(null);

        if (null == matchingObject){
            product.setProductId(0L);
            return productRepository.saveAndFlush(product);
        }
        return null;
    }

    public Product updateProduct(Product product) {
        logger.debug("Updating the product {}", product.toString());
        return productRepository.saveAndFlush(product);
    }

    public List<Unit> getAllUnits() {
        logger.debug("Getting all the units from repository");
        return unitRepository.findAll();
    }
}
