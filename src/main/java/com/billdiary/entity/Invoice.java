package com.billdiary.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "invoice")
@Data
public class Invoice {

    @Id
    private Long invoiceId;

    @ManyToOne
    @JoinColumn(name="customerId", nullable=false)
    private Customer customer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    private BigDecimal totalAmount;

/*    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp modifiedAt;*/

    @OneToMany(mappedBy = "invoice")
    private Set<InvoiceItem> invoiceItems;

}
