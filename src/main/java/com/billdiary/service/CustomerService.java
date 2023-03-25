package com.billdiary.service;

import java.sql.Timestamp;
import java.util.*;

import com.billdiary.config.AppConfig;
import com.billdiary.config.MessageConfig;
import com.billdiary.constant.Constant;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dto.CustomerDto;
import com.billdiary.exception.DatabaseException;
import com.billdiary.exception.DatabaseRuntimeException;
import com.billdiary.mapper.CustomerMapper;
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

    @Autowired
    CustomerMapper customerMapper;

    public List<CustomerDto> getCustomers()
    {
        LOGGER.info("fetching Customers");
        return customerMapper.customerToCustomerDto(customerRepository.findAll());
    }


    public List<CustomerDto> saveCustomers(List<CustomerDto> customerDtos) throws DatabaseException {
        LOGGER.info("Saving the customers {}", customerDtos);
        List<Customer>  updatedCustEntities = null;
        List<CustomerDto>  updatedCustDtos = null;
        try{
            List<Customer> customers = customerMapper.customerDtoToCustomer(customerDtos);
            Long maxId = getMaxCustomerId();
            customers.forEach(customer -> {
                customer.setCustomerId(maxId+1);
                //customer.setModifiedAt(getCurrentTimeStamp());
            });
            updatedCustEntities = customerRepository.saveAll(customers);
            updatedCustDtos = customerMapper.customerToCustomerDto(updatedCustEntities);

        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }

        return updatedCustDtos;
    }

    public CustomerDto updateCustomer(CustomerDto customerDto) throws DatabaseException {
        LOGGER.info("Entering in updateCustomer");
        Customer updateCustomer = null;
        CustomerDto updateCustomerDto = null;
        try {
            Customer customerEntity = customerMapper.customerDtoToCustomer(customerDto);
            Optional<Customer> existingCustomerOptional = customerRepository.findById(customerEntity.getCustomerId());
            if (existingCustomerOptional.isPresent()) {
                updateCustomer = existingCustomerOptional.get();
                NullAwareBeanUtils.copyNonNullProperties(customerEntity, updateCustomer);
                //updateCustomer.setModifiedAt(getCurrentTimeStamp());
                customerEntity = customerRepository.save(updateCustomer);
                updateCustomerDto = customerMapper.customerToCustomerDto(customerEntity);
            } else {
                throw new DatabaseException(ErrorConstants.Err_Code_502, messageConfig.getMessage(ErrorConstants.Err_Code_502));
            }
        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }
        return updateCustomerDto;
    }

    /**
     * All pagination methods
     */
    public long getCustomerCount() {
        long count=customerRepository.count();
        return count;
    }
    public List<CustomerDto> getCustomers(int index, int rowsPerPage) {
        LOGGER.info("Entering in getCustomers");
        List<Customer> customerList=new ArrayList<>();
        Page<Customer> customerEntities = customerRepository.findAll(PageRequest.of(index, rowsPerPage, Sort.Direction.ASC, "status"));
        LOGGER.info("Exiting in getCustomers");
        return customerMapper.customerToCustomerDto(customerEntities.toList());
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


    public CustomerDto getCustomerById(Long customerId) throws DatabaseException {
        Customer cust = null;
        CustomerDto customerDto = null;

        Optional<Customer> custEntity=customerRepository.findById(customerId);
        if(custEntity.isPresent()){
            cust = custEntity.get();
            customerDto = customerMapper.customerToCustomerDto(cust);
        }
        else{
            throw new DatabaseRuntimeException(ErrorConstants.Err_Code_502, messageConfig.getMessage(ErrorConstants.Err_Code_502));
        }
        return customerDto;
    }

    public Customer getCustomerEntityById(Long customerId) throws DatabaseException {
        Customer cust = null;
        Optional<Customer> custEntity=customerRepository.findById(customerId);
        if(custEntity.isPresent()){
            cust = custEntity.get();
        }
        else{
            throw new DatabaseRuntimeException(ErrorConstants.Err_Code_502, messageConfig.getMessage(ErrorConstants.Err_Code_502));
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
