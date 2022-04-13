package com.billdiary.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "supplier")
@Data
@NoArgsConstructor
@DynamicUpdate
public class Supplier {

    @Id
    @GeneratedValue
    private Long supplierId;

    @NonNull
    private String supplierName;
    private String firmName;
    private String address;
    private String mobileNo;


}
