package com.billdiary.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unit")
@Data
public class Unit {

    @Id
    @GeneratedValue
    private Long unitId;

    private String unitName;

}
