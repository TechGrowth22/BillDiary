package com.billdiary.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long productId;
    private String productCode;
    private String productName;
    private String productDescription;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id", referencedColumnName = "unitId")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name="productCategoryId", nullable=true)
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name="productBrandId", nullable=true)
    private ProductBrand productBrand;

}
