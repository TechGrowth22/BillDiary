package com.billdiary.service;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.ProductRepository;
import com.billdiary.dao.UnitRepository;
import com.billdiary.entity.Product;
import com.billdiary.entity.Unit;
import com.billdiary.exception.DatabaseException;
import com.billdiary.exception.DatabaseRuntimeException;
import com.billdiary.service.utility.NullAwareBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    MessageConfig messageConfig;

    public List<Product> getProducts() {
        logger.debug("Getting all the products from repository");
        return productRepository.findAll();
    }

    public List<Product> getProducts(int index,int rowsPerPage) throws DatabaseException {
        logger.debug("Entering in the getProducts");
        try {
            Page<Product> productEntities = productRepository.findAll(PageRequest.of(index, rowsPerPage));
            return productEntities.toList();
        }catch(Exception e) {
           throw new DatabaseException(ErrorConstants.Err_Code_501, messageConfig.getMessage(ErrorConstants.Err_Code_501));
        }
    }

    public Product getProductById(Long productId) throws DatabaseException {
        logger.debug("Getting product from repository productId {}", productId);
        Optional<Product> product=productRepository.findById(productId);
        if(product.isPresent()){
            return product.get();
        }
         else{
            throw new DatabaseException(ErrorConstants.Err_Code_503, messageConfig.getMessage(ErrorConstants.Err_Code_503));
        }
    }

    @Transactional
    public List<Product> saveProducts(List<Product> products) throws DatabaseRuntimeException{

        List<Product> savedProducts = new ArrayList<>();
        List<Product> failedToSaveProducts = new ArrayList<>();
        products.forEach(product ->{
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

            List<Product> productList = productRepository.findByproductCodeOrProductName(product.getProductCode(),product.getProductName());
            Product matchingObject = productList.stream().
                    filter(p -> p.getProductCode().equals(product.getProductCode()) || p.getProductName().equals(product.getProductName())).
                    findAny().orElse(null);

            if (null == matchingObject){
                product.setProductId(0L);
                savedProducts.add(productRepository.saveAndFlush(product));
            }else{
                throw new DatabaseRuntimeException(ErrorConstants.Err_Code_504, messageConfig.getMessage(ErrorConstants.Err_Code_504, product.getProductCode(), product.getProductName()));
            }

        });
        return savedProducts;
    }

    public Product updateProduct(Product product) throws DatabaseException {
        logger.debug("Updating the product {}", product.toString());
        Product product1= null;
        Optional<Product> optionalProduct = productRepository.findById(product.getProductId());

        if(optionalProduct.isPresent()){
            product1 = optionalProduct.get();
        }else{
         throw new DatabaseException(ErrorConstants.Err_Code_503, messageConfig.getMessage(ErrorConstants.Err_Code_503));
        }

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
