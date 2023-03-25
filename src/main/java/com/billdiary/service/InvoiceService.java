package com.billdiary.service;


import com.billdiary.config.AppConfig;
import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.InvoiceItemRepository;
import com.billdiary.dao.InvoiceRepository;
import com.billdiary.dto.CustomerDto;
import com.billdiary.dto.InvoiceDto;
import com.billdiary.dto.InvoiceItemDto;
import com.billdiary.entity.Customer;
import com.billdiary.entity.Invoice;
import com.billdiary.entity.InvoiceItem;
import com.billdiary.entity.Product;
import com.billdiary.exception.BusinessRuntimeException;
import com.billdiary.exception.DatabaseException;
import com.billdiary.mapper.CustomerMapper;
import com.billdiary.mapper.InvoiceItemMapper;
import com.billdiary.mapper.InvoiceMapper;
import com.billdiary.service.utility.NullAwareBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    final static Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    AppConfig appConfig;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    InvoiceItemMapper invoiceItemMapper;

    @Autowired
    ProductService productService;

    @Autowired
    InvoiceMapper invoiceMapper;

    @Autowired
    MessageConfig messageConfig;

    public List<InvoiceDto> getInvoices()
    {
        LOGGER.info("fetching Invoices");
        return invoiceMapper.invoiceToInvoiceDto(invoiceRepository.findAll());
    }


    @Transactional
    public InvoiceDto saveInvoices(InvoiceDto invoiceDto) throws DatabaseException, BusinessRuntimeException {
        LOGGER.info("Saving the Invoice {}", invoiceDto);
        Invoice invoice = null;
        try{
            invoice = invoiceMapper.invoiceDtoToInvoice(invoiceDto);
            Customer customer = customerService.getCustomerEntityById(invoiceDto.getCustomerId());
            invoice.setCustomer(customer);
            invoice.setInvoiceItems(null);
            invoice.setInvoiceId(getMaxInvoiceId()+1);
            invoice = invoiceRepository.save(invoice);

            Set<InvoiceItemDto> invoiceItemDtos = invoiceDto.getInvoiceItemDtos();
            Set<InvoiceItem> invoiceItems = new HashSet<>();
            Invoice finalInvoice = invoice;
            invoiceItemDtos.forEach(invoiceItemDto -> {
                InvoiceItem invoiceItem = invoiceItemMapper.invoiceItemDtoToInvoiceItem(invoiceItemDto);
                Product product = new Product();
                product.setProductId(invoiceItemDto.getProductId());
                invoiceItem.setProduct(product);
                invoiceItem.setInvoice(finalInvoice);
                invoiceItems.add(invoiceItem);
            });

            List<InvoiceItem> updatedInvoiceItems= invoiceItemRepository.saveAll(invoiceItems);
            invoice.setInvoiceItems(new HashSet<>(updatedInvoiceItems));
            invoice = invoiceRepository.save(invoice);
            invoiceDto = invoiceMapper.invoiceToInvoiceDto(invoice);
            invoiceDto.setInvoiceItemDtos(new HashSet<>(invoiceItemMapper.invoiceItemToInvoiceItemDto(updatedInvoiceItems)));
        }catch(BusinessRuntimeException be){
           throw be;
        } catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }
        return invoiceDto;
    }

    private Invoice getInvoiceOfEmptyInvoiceItems(Invoice invoice) throws DatabaseException {
        Invoice emptyInvoice = new Invoice();
        emptyInvoice.setInvoiceId(getMaxInvoiceId()+1);
        emptyInvoice.setInvoiceDate(invoice.getInvoiceDate());
        emptyInvoice.setTotalAmount(invoice.getTotalAmount());
        if(null == invoice.getCustomer())
            throw new BusinessRuntimeException(ErrorConstants.Err_Code_521, messageConfig.getMessage(ErrorConstants.Err_Code_521));
        CustomerDto customerDto = customerService.getCustomerById(invoice.getCustomer().getCustomerId());
        emptyInvoice.setCustomer(customerMapper.customerDtoToCustomer(customerDto));
        return emptyInvoice;
    }

    public InvoiceDto updateInvoice(Invoice invoice) throws DatabaseException {
        LOGGER.info("Entering in updateInvoice");
        Invoice updateInvoice = null;
        try {
            Optional<Invoice> existingInvoiceOptional = invoiceRepository.findById(invoice.getInvoiceId());
            if (existingInvoiceOptional.isPresent()) {
                updateInvoice = existingInvoiceOptional.get();
                NullAwareBeanUtils.copyNonNullProperties(invoice, updateInvoice);
                //updateInvoice.setModifiedAt(getCurrentTimeStamp());
                invoiceRepository.save(updateInvoice);
            } else {
                throw new DatabaseException(ErrorConstants.Err_Code_520, messageConfig.getMessage(ErrorConstants.Err_Code_520, invoice.getInvoiceId()));
            }
        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }
        return invoiceMapper.invoiceToInvoiceDto(updateInvoice);
    }

    /**
     * All pagination methods
     */
    public long getInvoiceCount() {
        long count=invoiceRepository.count();
        return count;
    }
    public List<InvoiceDto> getInvoices(int index,int rowsPerPage) {
        LOGGER.info("Entering in getInvoices");
        Page<Invoice> invoiceEntities = invoiceRepository.findAll(PageRequest.of(index, rowsPerPage));
        LOGGER.info("Exiting in getInvoices");
        return invoiceMapper.invoiceToInvoiceDto(invoiceEntities.toList());
    }

    public InvoiceDto getInvoiceById(Long invoiceId) throws DatabaseException {
        Invoice invoice = null;
        InvoiceDto invoiceDto = null;
        Optional<Invoice> invoiceEntity=invoiceRepository.findById(invoiceId);
        if(invoiceEntity.isPresent()){
            invoice = invoiceEntity.get();
            invoiceDto = invoiceMapper.invoiceToInvoiceDto(invoice);
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_520, messageConfig.getMessage(ErrorConstants.Err_Code_520, invoiceId));
        }
        return invoiceDto;
    }

    public boolean deleteInvoice(long invoiceId) throws DatabaseException {
        if(isInvoiceAlreadyExists(invoiceId)){
            LOGGER.info("Deleting the Invoice {}", invoiceId);
            invoiceRepository.deleteById(invoiceId);
        }
        return true;
    }

    private boolean isInvoiceAlreadyExists(Long invoiceId) throws DatabaseException {
        Optional<Invoice> invoiceEntity=invoiceRepository.findById(invoiceId);
        if(invoiceEntity.isPresent()){
            return true;
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_520, messageConfig.getMessage(ErrorConstants.Err_Code_520, invoiceId));
        }
    }

    private Long getMaxInvoiceId(){
        Long maxId = invoiceRepository.maxInvoiceId();
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

