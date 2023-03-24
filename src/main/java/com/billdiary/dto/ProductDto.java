package com.billdiary.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private Long productId;
    private String productCode;
    private String productName;
    private String productDescription;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal quantity;
    private UnitDto unitDto;
    private ProductCategoryDto productCategoryDto;
    private ProductBrandDto productBrandDto;
}
