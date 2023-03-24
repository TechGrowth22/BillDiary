package com.billdiary.mapper;


import com.billdiary.dto.CustomerDto;
import com.billdiary.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper {

    public abstract Customer customerDtoToCustomer(CustomerDto customerDto);

    public abstract CustomerDto customerToCustomerDto(Customer customer);

    public abstract List<Customer> customerDtoToCustomer(List<CustomerDto> customerDtos);

    public abstract List<CustomerDto> customerToCustomerDto(List<Customer> customers);
}
