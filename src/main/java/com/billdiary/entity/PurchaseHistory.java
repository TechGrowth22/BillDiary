package com.billdiary.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_history")
@Data
public class PurchaseHistory {

    @Id
    @GeneratedValue
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name="supplier_id", nullable=false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
}
