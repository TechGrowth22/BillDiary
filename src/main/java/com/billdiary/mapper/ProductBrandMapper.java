package com.billdiary.mapper;

import com.billdiary.dto.ProductBrandDto;
import com.billdiary.entity.ProductBrand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductBrandMapper {

    public abstract ProductBrand productBrandDtoToProductBrand(ProductBrandDto productBrandDto);

    public abstract ProductBrandDto productBrandToProductBrandDto(ProductBrand productBrand);

    public abstract List<ProductBrand> productBrandDtoToProductBrand(List<ProductBrandDto> productBrandDtos);

    public abstract List<ProductBrandDto> productBrandToProductBrandDto(List<ProductBrand> productBrands);
}
