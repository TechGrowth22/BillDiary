package com.billdiary.dto;

import com.billdiary.entity.Invoice;
import com.billdiary.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemDto {

    private Long invoiceItemId;
    private Long invoiceId;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal discount;
}
