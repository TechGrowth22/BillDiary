package com.billdiary.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "supplier")
@Data
public class Supplier {

    @Id
    @GeneratedValue
    private Long supplierId;

    private String supplierName;
    private String firmName;
    private String address;
    private String mobileNo;
}
