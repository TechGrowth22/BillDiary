package com.billdiary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "unit")
@Data
public class Unit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long unitId;

    private String unitName;

}
