package com.billdiary.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    @GeneratedValue
    private Long customerId;
    private String fullName;
    private String address;
    private String mobileNo;
    private LocalDate birthdate;
}
