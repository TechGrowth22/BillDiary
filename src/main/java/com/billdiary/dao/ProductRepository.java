package com.billdiary.dao;

import com.billdiary.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByproductCodeOrProductName(String productCode, String ProductName);
    Product findByProductCode(String productCode);

}
