package com.billdiary.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_product")
@Getter
@Setter
public class InvoiceItem {

    @Id
    @GeneratedValue
    private Long invoiceItemId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="invoiceId", nullable=false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name="productId", nullable=false)
    private Product product;

    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal discount;

}
