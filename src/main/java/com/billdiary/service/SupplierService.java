package com.billdiary.service;

import com.billdiary.dao.SupplierRepository;
import com.billdiary.entity.Supplier;
import com.billdiary.service.utility.NullAwareBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);

    @Autowired
    SupplierRepository supplierRepository;

    public List<Supplier> getSuppliers() {
        logger.debug("Getting all the products from repository");
        return supplierRepository.findAll();
    }

    public Object getSupplierById(Long supplierId) {

        logger.debug("Getting product from repository productId {}", supplierId);
        return supplierRepository.findById(supplierId).get();
    }

    public Supplier createSupplier(Supplier supplier) {

        logger.debug("Creating a product {}", supplier.toString());

        List<Supplier> suppliers = supplierRepository.findBySupplierIdOrSupplierName(supplier.getSupplierId(), supplier.getSupplierName());
        Supplier matchingObject = suppliers.stream().
                filter(s ->  s.getSupplierId().equals(supplier.getSupplierId()) || s.getSupplierName().equals(supplier.getSupplierName())).
                findAny().orElse(null);

        if (null == matchingObject){
            supplier.setSupplierId(0L);
            return supplierRepository.saveAndFlush(supplier);
        }
        return null;
    }

    public Supplier updateSupplier(Supplier supplier) {
        logger.debug("Updating the product {}", supplier.toString());

        List<Supplier> supplierList = supplierRepository.findBySupplierId(supplier.getSupplierId());
        if (null != supplierList && supplierList.size()>0){
            Supplier supplier1 = supplierList.get(0);

            NullAwareBeanUtils.copyNonNullProperties(supplier,supplier1,"");
            //BeanUtils.copyProperties(supplier, supplier1);

            return supplierRepository.saveAndFlush(supplier1);
        }else{
            logger.debug("Supplier can not be updated cause It is not exits");
            return null;
        }
    }
}
