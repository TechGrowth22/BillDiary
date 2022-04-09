package com.billdiary.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
@Data
public class Invoice {

    @Id
    @GeneratedValue
    private String invoiceId;
    private String customerId;
    private LocalDate invoiceDate;
    private BigDecimal total;

}
