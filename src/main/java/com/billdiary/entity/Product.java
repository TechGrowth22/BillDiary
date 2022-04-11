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
    private Long productCode;
    private String productName;
    private String productType;
    private BigDecimal sellPrice;
    private BigDecimal quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id", referencedColumnName = "unitId")
    private Unit unit;
}
