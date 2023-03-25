package com.billdiary.dto;

import com.billdiary.entity.InvoiceItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;


@Getter
@Setter
public class InvoiceDto {

    private Long invoiceId;
    private Long customerId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    private BigDecimal totalAmount;
    private Set<InvoiceItemDto> invoiceItemDtos;

}
