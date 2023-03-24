package com.billdiary.mapper;


import com.billdiary.dto.ProductDto;
import com.billdiary.dto.ProductDto;
import com.billdiary.dto.UnitDto;
import com.billdiary.entity.Product;
import com.billdiary.entity.Product;
import com.billdiary.entity.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Mapping(target="unit", source="productDto.unitDto")
    @Mapping(target="productCategory", source="productDto.productCategoryDto")
    @Mapping(target="productBrand", source="productDto.productBrandDto")
    public abstract Product productDtoToProduct(ProductDto productDto);


    @Mapping(target="unitDto", source="product.unit")
    @Mapping(target="productCategoryDto", source="product.productCategory")
    @Mapping(target="productBrandDto", source="product.productBrand")
    public abstract ProductDto productToProductDto(Product product);


    public abstract List<Product> productDtoToProduct(List<ProductDto> productDtos);

    public abstract List<ProductDto> productToProductDto(List<Product> products);

}
