package com.billdiary.service;


import com.billdiary.config.AppConfig;
import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.InvoiceItemRepository;
import com.billdiary.dao.InvoiceRepository;
import com.billdiary.dto.CustomerDto;
import com.billdiary.entity.Customer;
import com.billdiary.entity.Invoice;
import com.billdiary.entity.InvoiceItem;
import com.billdiary.exception.BusinessRuntimeException;
import com.billdiary.exception.DatabaseException;
import com.billdiary.mapper.CustomerMapper;
import com.billdiary.service.utility.NullAwareBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
    MessageConfig messageConfig;

    public List<Invoice> getInvoices()
    {
        LOGGER.info("fetching Invoices");
        return invoiceRepository.findAll();
    }


    public Invoice saveInvoices(Invoice invoice) throws DatabaseException, BusinessRuntimeException {
        LOGGER.info("Saving the Invoice {}", invoice);
        Invoice inv = null;
        try{

            // 1. Create a invoice with empty invoice items
            inv = getInvoiceOfEmptyInvoiceItems(invoice);
            inv = invoiceRepository.save(inv);
            // 2. add invoice Id to all invoice items
            Invoice finalInv = inv;
            invoice.getInvoiceItems().forEach(invoiceItem -> {
                invoiceItem.setInvoice(finalInv);
            });
            List<InvoiceItem> items = invoiceItemRepository.saveAll(invoice.getInvoiceItems());
            Set<InvoiceItem> invItems  = items.stream().collect(Collectors.toSet());
            // 3. update the invoice
            inv.setInvoiceItems(invItems);
            inv = invoiceRepository.save(inv);
            //updatedCustEntities = invoiceRepository.saveAll(invoices);
        }catch(BusinessRuntimeException be){
           throw be;
        } catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }
        return inv;
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

    public Invoice updateInvoice(Invoice invoice) throws DatabaseException {
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
        return updateInvoice;
    }

    /**
     * All pagination methods
     */
    public long getInvoiceCount() {
        long count=invoiceRepository.count();
        return count;
    }
    public List<Invoice> getInvoices(int index,int rowsPerPage) {
        LOGGER.info("Entering in getInvoices");
        List<Invoice> InvoiceList=new ArrayList<>();
        Page<Invoice> InvoiceEntities = invoiceRepository.findAll(PageRequest.of(index, rowsPerPage));
        LOGGER.info("Exiting in getInvoices");
        return InvoiceEntities.toList();
    }

    public Invoice getInvoiceById(Long invoiceId) throws DatabaseException {
        Invoice invoice = null;
        Optional<Invoice> invoiceEntity=invoiceRepository.findById(invoiceId);
        if(invoiceEntity.isPresent()){
            invoice = invoiceEntity.get();
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_520, messageConfig.getMessage(ErrorConstants.Err_Code_520, invoiceId));
        }
        return invoice;
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

