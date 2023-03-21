package com.billdiary.service;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.ProductCategoryRepository;
import com.billdiary.entity.ProductCategory;
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
public class ProductCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryService.class);

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    MessageConfig messageConfig;

    public List<ProductCategory> getProductCategories(int index, int rowsPerPage) throws DatabaseException {
        logger.debug("Entering in the getProductCategories");
        try {
            Page<ProductCategory> productCategoryEntities = productCategoryRepository.findAll(PageRequest.of(index, rowsPerPage));
            return productCategoryEntities.toList();
        }catch(Exception e) {
            throw new DatabaseException(ErrorConstants.Err_Code_501, messageConfig.getMessage(ErrorConstants.Err_Code_501));
        }
    }

    public ProductCategory getProductCategoryById(Long productCategoryId) throws DatabaseException {

        logger.debug("Getting product from repository productId {}", productCategoryId);
        Optional<ProductCategory> productCategory=productCategoryRepository.findById(productCategoryId);
        if(productCategory.isPresent()){
            return productCategory.get();
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_505, messageConfig.getMessage(ErrorConstants.Err_Code_505));
        }
    }

    @Transactional
    public List<ProductCategory> saveProductCategories(List<ProductCategory> productCategories) {

        List<ProductCategory> savedProductCategories = new ArrayList<>();
        List<ProductCategory> failedToSaveProducts = new ArrayList<>();
        productCategories.forEach(productCategory ->{


            List<ProductCategory> productCategoryList = productCategoryRepository.findByProductHsnCodeOrProductCategoryName(productCategory.getProductHsnCode(),productCategory.getProductCategoryName());
            ProductCategory matchingObject = productCategoryList.stream().
                    filter(p -> p.getProductHsnCode().equals(productCategory.getProductHsnCode()) || p.getProductCategoryName().equals(productCategory.getProductCategoryName())).
                    findAny().orElse(null);

            if (null == matchingObject){
                productCategory.setProductCategoryId(0L);
                savedProductCategories.add(productCategoryRepository.saveAndFlush(productCategory));
            }else{
                throw new DatabaseRuntimeException(ErrorConstants.Err_Code_506, messageConfig.getMessage(ErrorConstants.Err_Code_506, productCategory.getProductHsnCode(), productCategory.getProductCategoryName()));
            }

        });
        return savedProductCategories;
    }

    public ProductCategory updateProductCategory(ProductCategory productCategory) throws DatabaseException {

        logger.debug("Updating the product {}", productCategory.toString());
        ProductCategory productCategory1= null;
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(productCategory.getProductCategoryId());

        if(optionalProductCategory.isPresent()){
            productCategory1 = optionalProductCategory.get();
        }else{
            throw new DatabaseException(ErrorConstants.Err_Code_505, messageConfig.getMessage(ErrorConstants.Err_Code_505, String.valueOf(productCategory.getProductCategoryId())));
        }

        if(null != productCategory1)
            NullAwareBeanUtils.copyNonNullProperties(productCategory,productCategory1,"");

        return productCategoryRepository.saveAndFlush(productCategory1);
    }

    public boolean deleteProductCategory(Long productCategoryId) throws DatabaseException {
        if(isProductCategoryAlreadyExists(productCategoryId)){
            logger.info("Deleting the customer {}", productCategoryId);
            productCategoryRepository.deleteById(productCategoryId);
        }
        return true;
    }

    private boolean isProductCategoryAlreadyExists(Long productCategoryId) throws DatabaseException {
        Optional<ProductCategory> productCategoryEntity=productCategoryRepository.findById(productCategoryId);
        if(productCategoryEntity.isPresent()){
            return true;
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_505, messageConfig.getMessage(ErrorConstants.Err_Code_505, String.valueOf(productCategoryId)));
        }
    }
}
