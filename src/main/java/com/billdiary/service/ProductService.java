package com.billdiary.service;

import com.billdiary.controller.ProductController;
import com.billdiary.dao.ProductRepository;
import com.billdiary.dao.UnitRepository;
import com.billdiary.entity.Product;
import com.billdiary.entity.Unit;
import com.billdiary.service.utility.NullAwareBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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
        Product product1= null;
        Optional<Product> optionalProduct = productRepository.findById(product.getProductId());

        if(optionalProduct.isPresent()){
            product1 = optionalProduct.get();
        }else
            return null;

        if(null != product.getProductCode()){
            Product product2 = productRepository.findByProductCode(product.getProductCode());
            if(null != product2 && product2.getProductId() != product.getProductId())
                return null;
        }

        if(null != product.getUnit()){
            List<Unit> units = unitRepository.findByUnitIdOrUnitName(product.getUnit().getUnitId(),product.getUnit().getUnitName());
            if(null != units && units.size()>0)
                product.setUnit(units.get(0));
            else{
                Unit u = new Unit();
                u.setUnitId(1l);
                u.setUnitName("KG");
                product.setUnit(u);
            }
        }
        if(null != product1)
            NullAwareBeanUtils.copyNonNullProperties(product,product1,"");

        return productRepository.saveAndFlush(product1);
    }

    public List<Unit> getAllUnits() {
        logger.debug("Getting all the units from repository");
        return unitRepository.findAll();
    }
}
