package com.billdiary.service;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.ProductCategoryRepository;
import com.billdiary.entity.Product;
import com.billdiary.entity.ProductCategory;
import com.billdiary.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
