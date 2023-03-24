package com.billdiary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

@Data
public class CustomerDto {

    private Long customerId;
    private String fullName;
    private String address;
    private String mobileNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private Double balance;
    private String status;
}
