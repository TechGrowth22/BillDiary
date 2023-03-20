package com.billdiary.service;

import java.sql.Timestamp;
import java.util.*;

import com.billdiary.config.AppConfig;
import com.billdiary.config.MessageConfig;
import com.billdiary.constant.Constant;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.exception.DatabaseException;
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

    @Autowired
    AppConfig appConfig;

    @Autowired
    MessageConfig messageConfig;

    public List<Customer> getCustomers()
    {
        LOGGER.info("fetching Customers");
        return customerRepository.findAll();
    }


    public List<Customer> saveCustomers(List<Customer> customers)
    {
        LOGGER.info("Saving the customers {}", customers);
        Long maxId = getMaxCustomerId();
        customers.forEach(customer -> {
            customer.setCustomerId(maxId+1);
            customer.setModifiedAt(getCurrentTimeStamp());
        });
        List<Customer>  updatedCustEntities = customerRepository.saveAll(customers);
        return updatedCustEntities;
    }

    public Customer updateCustomer(Customer customer) throws DatabaseException {
        LOGGER.info("Entering in updateCustomer");
        Customer updateCustomer = null;
        Optional<Customer> existingCustomerOptional = customerRepository.findById(customer.getCustomerId());
        if(existingCustomerOptional.isPresent()){
            updateCustomer = existingCustomerOptional.get();
            NullAwareBeanUtils.copyNonNullProperties(customer, updateCustomer);
            updateCustomer.setModifiedAt(getCurrentTimeStamp());
            customerRepository.save(updateCustomer);
        }else{
            throw new DatabaseException(ErrorConstants.Err_Code_502, messageConfig.getMessage(ErrorConstants.Err_Code_502));
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

    public Customer getCustomerById(Long customerId) throws DatabaseException {
        Customer cust = null;
        Optional<Customer> custEntity=customerRepository.findById(customerId);
        if(custEntity.isPresent()){
            cust = custEntity.get();
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_502, messageConfig.getMessage(ErrorConstants.Err_Code_502));
        }
        return cust;
    }

    public boolean deleteCustomer(long customerId) throws DatabaseException {
        if(isCustomerAlreadyExists(customerId)){
            LOGGER.info("Deleting the customer {}", customerId);
            customerRepository.deleteById(customerId);
        }
        return true;
    }

    private boolean isCustomerAlreadyExists(Long customerId) throws DatabaseException {
        Optional<Customer> custEntity=customerRepository.findById(customerId);
        if(custEntity.isPresent()){
            return true;
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_502, messageConfig.getMessage(ErrorConstants.Err_Code_502));
        }
    }

    private Long getMaxCustomerId(){
        Long maxId = customerRepository.maxCustomerId();
        if(maxId != null){
            return maxId;
        }else{
           return appConfig.getStartingId();
        }
    }

    private Timestamp getCurrentTimeStamp(){
        return new Timestamp(new Date().getTime());
    }
}
