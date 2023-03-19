package com.billdiary.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.billdiary.constant.Constant;
import com.billdiary.service.utility.NullAwareBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.billdiary.entity.Customer;
import com.billdiary.dao.CustomerRepository;

@Service
public class CustomerService {

    final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getCustomers()
    {
        LOGGER.info("fetching Customers");
        return customerRepository.findAll();
    }

    public boolean deleteCustomer(long id)
    {
        boolean customerDeleted=false;
        LOGGER.info("Deleting the customer {}", id);
        customerRepository.deleteById(id);
        return true;
    }
    public List<Customer> saveCustomers(List<Customer> customers)
    {
        LOGGER.info("Saving the customers {}", customers);
        List<Customer>  updatedCustEntities = customerRepository.saveAll(customers);
        return updatedCustEntities;
    }

    public Customer updateCustomer(Customer customer)
    {
        LOGGER.info("Entering in updateCustomer");
        Customer updateCustomer = null;
        Optional<Customer> existingCustomerOptional = customerRepository.findById(customer.getCustomerId());
        if(existingCustomerOptional.isPresent()){
            updateCustomer = existingCustomerOptional.get();
            NullAwareBeanUtils.copyNonNullProperties(updateCustomer, customer);
            customerRepository.save(updateCustomer);
        }
        return updateCustomer;
    }

    /**
     * All pagination methods
     */
    public long getCustomerCount() {
        long count=customerRepository.count();
        return count;
    }
    public List<Customer> getCustomers(int index,int rowsPerPage) {
        LOGGER.info("Entering in getCustomers");
        List<Customer> customerList=new ArrayList<>();
        Page<Customer> customerEntities = customerRepository.findAll(PageRequest.of(index, rowsPerPage, Sort.Direction.ASC, "status"));
        LOGGER.info("Exiting in getCustomers");
        return customerEntities.toList();
    }

    public void updateCustomerBalance(Long customerId, Double amount) {
        customerRepository.updateCustomerBalance(customerId,amount);
    }


    public Customer getDefaultCustomer() {
        Customer custEntity=customerRepository.findByFullNameAndAddressAndMobileNo(Constant.DEFAULT_CUSTOMER_NAME,Constant.DEFAULT_CUSTOMER_ADDRESS,Constant.DEFAULT_CUSTOMER_MOBILE_NO);
        if(null == custEntity) {
            custEntity=new Customer();
            custEntity.setCustomerId(0L);
            custEntity.setFullName(Constant.DEFAULT_CUSTOMER_NAME);
            custEntity.setAddress(Constant.DEFAULT_CUSTOMER_ADDRESS);
            custEntity.setMobileNo(Constant.DEFAULT_CUSTOMER_MOBILE_NO);
            custEntity.setStatus(Constant.ACTIVE);
            customerRepository.save(custEntity);
            custEntity=customerRepository.findByFullNameAndAddressAndMobileNo(Constant.DEFAULT_CUSTOMER_NAME,Constant.DEFAULT_CUSTOMER_ADDRESS,Constant.DEFAULT_CUSTOMER_MOBILE_NO);
        }
        return custEntity;
    }

    public Customer addNewCustomerByInvoive(Customer customer) {
        Customer custEntity = customerRepository.saveAndFlush(customer);
        return custEntity;
    }

    public Customer getCustomerById(Long custId) {
        Customer cust = new Customer();
        try {
            Optional<Customer> custEntity=customerRepository.findById(custId);
            if(custEntity.isPresent())
                cust = custEntity.get();
        }catch(Exception e) {
            LOGGER.error("Exception Occured"+e);
        }
        return cust;
    }
}
