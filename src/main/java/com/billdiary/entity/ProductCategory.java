package com.billdiary.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_category")
@Data
public class ProductCategory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productCategoryId;
    private String productHsnCode;
    private String productCategoryName;
    private String ProductCategoryDescription;
    private BigDecimal igst;
    private BigDecimal cgst;
    private BigDecimal sgst;

}
