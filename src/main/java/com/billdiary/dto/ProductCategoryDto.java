package com.billdiary.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductCategoryDto {

    private Long productCategoryId;
    private String productHsnCode;
    private String productCategoryName;
    private String ProductCategoryDescription;
    private BigDecimal igst;
    private BigDecimal cgst;
    private BigDecimal sgst;
}
