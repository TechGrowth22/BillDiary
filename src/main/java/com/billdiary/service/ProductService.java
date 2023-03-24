package com.billdiary.service;

import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.ProductRepository;
import com.billdiary.dao.UnitRepository;
import com.billdiary.dto.ProductDto;
import com.billdiary.entity.Product;
import com.billdiary.entity.Unit;
import com.billdiary.exception.DatabaseException;
import com.billdiary.exception.DatabaseRuntimeException;
import com.billdiary.mapper.ProductMapper;
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
    ProductMapper productMapper;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    MessageConfig messageConfig;

    public List<ProductDto> getProducts() {
        logger.debug("Getting all the products from repository");
        return productMapper.productToProductDto(productRepository.findAll());
    }

    public List<ProductDto> getProducts(int index,int rowsPerPage) throws DatabaseException {
        logger.debug("Entering in the getProducts");
        try {
            Page<Product> productEntities = productRepository.findAll(PageRequest.of(index, rowsPerPage));
            return productMapper.productToProductDto(productEntities.toList());
        }catch(Exception e) {
           throw new DatabaseException(ErrorConstants.Err_Code_501, messageConfig.getMessage(ErrorConstants.Err_Code_501));
        }
    }

    public ProductDto getProductById(Long productId) throws DatabaseException {
        logger.debug("Getting product from repository productId {}", productId);
        Optional<Product> product=productRepository.findById(productId);
        if(product.isPresent()){
            return productMapper.productToProductDto(product.get());
        }
         else{
            throw new DatabaseException(ErrorConstants.Err_Code_503, messageConfig.getMessage(ErrorConstants.Err_Code_503));
        }
    }

    @Transactional
    public List<ProductDto> saveProducts(List<ProductDto> productDtos) throws DatabaseRuntimeException, DatabaseException {
        List<ProductDto> savedProductDtos = new ArrayList<>();
        try{
            List<Product> failedToSaveProducts = new ArrayList<>();
            List<Product> products = productMapper.productDtoToProduct(productDtos);
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
                    savedProductDtos.add(productMapper.productToProductDto(productRepository.saveAndFlush(product)));
                }else{
                    throw new DatabaseRuntimeException(ErrorConstants.Err_Code_504, messageConfig.getMessage(ErrorConstants.Err_Code_504, product.getProductCode(), product.getProductName()));
                }

            });
        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }
        return savedProductDtos;
    }

    public ProductDto updateProduct(ProductDto productDto) throws DatabaseException {
        logger.debug("Updating the product {}", productDto.toString());
        Product product1= null;
        ProductDto productDto1= null;
        try{
            Product product = productMapper.productDtoToProduct(productDto);
            Optional<Product> optionalProduct = productRepository.findById(product.getProductId());
            if(optionalProduct.isPresent()){
                product1 = optionalProduct.get();
            }else{
                throw new DatabaseException(ErrorConstants.Err_Code_503, messageConfig.getMessage(ErrorConstants.Err_Code_503, product.getProductId()));
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

            product1 = productRepository.saveAndFlush(product1);
            productDto1 = productMapper.productToProductDto(product1);

        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }

        return productDto1;
    }

}
