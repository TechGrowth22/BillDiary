package com.billdiary.dao;

import com.billdiary.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository  extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByProductHsnCodeOrProductCategoryName(String productHsnCode, String productCategoryName);
}
