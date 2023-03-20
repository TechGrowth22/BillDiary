package com.billdiary.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    private Long customerId;
    private String fullName;
    private String address;
    private String mobileNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private Double balance;
    private String status;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp modifiedAt;
}
