package com.billdiary.service;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.ProductCategoryRepository;
import com.billdiary.dto.ProductCategoryDto;
import com.billdiary.entity.ProductCategory;
import com.billdiary.exception.DatabaseException;
import com.billdiary.exception.DatabaseRuntimeException;
import com.billdiary.mapper.ProductCategoryMapper;
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
    ProductCategoryMapper productCategoryMapper;

    @Autowired
    MessageConfig messageConfig;

    public List<ProductCategoryDto> getProductCategories(int index, int rowsPerPage) throws DatabaseException {
        logger.debug("Entering in the getProductCategories");
        try {
            Page<ProductCategory> productCategoryEntities = productCategoryRepository.findAll(PageRequest.of(index, rowsPerPage));
            return productCategoryMapper.productCategoryToProductCategoryDto(productCategoryEntities.toList());
        }catch(Exception e) {
            throw new DatabaseException(ErrorConstants.Err_Code_501, messageConfig.getMessage(ErrorConstants.Err_Code_501));
        }
    }

    public ProductCategoryDto getProductCategoryById(Long productCategoryId) throws DatabaseException {

        logger.debug("Getting product from repository productId {}", productCategoryId);
        Optional<ProductCategory> productCategory=productCategoryRepository.findById(productCategoryId);
        if(productCategory.isPresent()){
            return productCategoryMapper.productCategoryToProductCategoryDto(productCategory.get());
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_505, messageConfig.getMessage(ErrorConstants.Err_Code_505));
        }
    }

    @Transactional
    public List<ProductCategoryDto> saveProductCategories(List<ProductCategoryDto> productCategorieDtos) throws DatabaseException {
        List<ProductCategoryDto> savedProductCategorieDtos = new ArrayList<>();
        try{

            List<ProductCategory> failedToSaveProducts = new ArrayList<>();
            List<ProductCategory> productCategories = productCategoryMapper.productCategoryDtoToProductCategory(productCategorieDtos);
            productCategories.forEach(productCategory ->{


                List<ProductCategory> productCategoryList = productCategoryRepository.findByProductHsnCodeOrProductCategoryName(productCategory.getProductHsnCode(),productCategory.getProductCategoryName());
                ProductCategory matchingObject = productCategoryList.stream().
                        filter(p -> p.getProductHsnCode().equals(productCategory.getProductHsnCode()) || p.getProductCategoryName().equals(productCategory.getProductCategoryName())).
                        findAny().orElse(null);

                if (null == matchingObject){
                    productCategory.setProductCategoryId(0L);
                    savedProductCategorieDtos.add(productCategoryMapper.productCategoryToProductCategoryDto(productCategoryRepository.saveAndFlush(productCategory)));
                }else{
                    throw new DatabaseRuntimeException(ErrorConstants.Err_Code_506, messageConfig.getMessage(ErrorConstants.Err_Code_506, productCategory.getProductHsnCode(), productCategory.getProductCategoryName()));
                }

            });
        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }

        return savedProductCategorieDtos;
    }

    public ProductCategoryDto updateProductCategory(ProductCategoryDto productCategoryDto) throws DatabaseException {

        logger.debug("Updating the product {}", productCategoryDto.toString());
        ProductCategory productCategory1= null;
        ProductCategoryDto productCategoryDto1= null;
        try{
            ProductCategory productCategory = productCategoryMapper.productCategoryDtoToProductCategory(productCategoryDto);
            Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(productCategory.getProductCategoryId());
            if(optionalProductCategory.isPresent()){
                productCategory1 = optionalProductCategory.get();
            }else{
                throw new DatabaseException(ErrorConstants.Err_Code_505, messageConfig.getMessage(ErrorConstants.Err_Code_505, String.valueOf(productCategory.getProductCategoryId())));
            }

            if(null != productCategory1)
                NullAwareBeanUtils.copyNonNullProperties(productCategory,productCategory1,"");

            productCategory1 = productCategoryRepository.saveAndFlush(productCategory1);
            productCategoryDto1 = productCategoryMapper.productCategoryToProductCategoryDto(productCategory1);

        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }
        return productCategoryDto1;
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
