package com.billdiary.dao;

import com.billdiary.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findBySupplierId(Long supplierId);

    List<Supplier> findBySupplierIdOrSupplierName(Long supplierId, String supplierName);
}
