package com.billdiary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;


@Getter
@Setter
public class InvoiceModel {

    private Long invoiceId;

    private Long customerId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    private BigDecimal totalAmount;

}
