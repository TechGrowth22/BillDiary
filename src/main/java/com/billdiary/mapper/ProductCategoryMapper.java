package com.billdiary.mapper;

import com.billdiary.dto.ProductCategoryDto;
import com.billdiary.entity.ProductCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductCategoryMapper {

    public abstract ProductCategory productCategoryDtoToProductCategory(ProductCategoryDto productCategoryDto);

    public abstract ProductCategoryDto productCategoryToProductCategoryDto(ProductCategory productCategory);

    public abstract List<ProductCategory> productCategoryDtoToProductCategory(List<ProductCategoryDto> productCategoryDtos);

    public abstract List<ProductCategoryDto> productCategoryToProductCategoryDto(List<ProductCategory> productCategorys);
}
