package com.billdiary.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    @SequenceGenerator(name = "pet_seq",
            sequenceName = "pet_sequence",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    private Long customerId;
    private String fullName;
    private String address;
    private String mobileNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private Double balance;
    private String status;
}
