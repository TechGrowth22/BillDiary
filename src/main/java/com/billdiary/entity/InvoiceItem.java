package com.billdiary.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_product")
@Data
public class InvoiceItem {

    @Id
    @GeneratedValue
    private Long invoiceItemId;

    @ManyToOne
    @JoinColumn(name="invoiceId", nullable=false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name="productId", nullable=false)
    private Product product;

    BigDecimal quantity;
    BigDecimal price;
    BigDecimal discount;

}
