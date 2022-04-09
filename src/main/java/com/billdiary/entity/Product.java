package com.billdiary.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long productId;
    private String productName;
    private String productType;
    private BigDecimal sellPrice;
    private BigDecimal quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id", referencedColumnName = "unitId")
    private Unit unit;
}
