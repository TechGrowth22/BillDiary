package com.billdiary.dao;

import com.billdiary.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Query("update Customer customer set customer.balance=:balance where customer.customerId=:customerId")
    @Transactional
    public int updateCustomerBalance(@Param("customerId") Long customerId, @Param("balance") double balance);

    public Customer findByFullNameAndAddressAndMobileNo(String fullName,String address,String mobile_no);
    @Query("SELECT MAX(customer.customerId) FROM Customer customer")
    public Long maxCustomerId();


}