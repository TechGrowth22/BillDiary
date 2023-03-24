package com.billdiary.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "product_brand")
@Data
public class ProductBrand {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productBrandId;
    private String productBrandName;

}
