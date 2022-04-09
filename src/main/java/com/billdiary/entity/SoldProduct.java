package com.billdiary.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sold_product")
@Data
public class SoldProduct {

    @Id
    @GeneratedValue
    private Long soldProductId;

    @ManyToOne
    @JoinColumn(name="invoice_id", nullable=false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;
}
