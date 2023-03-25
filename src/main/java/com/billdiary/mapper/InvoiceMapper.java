package com.billdiary.mapper;

import com.billdiary.dto.InvoiceDto;
import com.billdiary.dto.InvoiceItemDto;
import com.billdiary.entity.Invoice;
import com.billdiary.entity.InvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class InvoiceMapper {

    @Mapping(target="invoiceItems", source="invoiceDto.invoiceItemDtos")
    public abstract Invoice invoiceDtoToInvoice(InvoiceDto invoiceDto);

    @Mapping(target="invoiceItemDtos", source="invoice.invoiceItems")
    @Mapping(target="customerId", source="customer.customerId")
    public abstract InvoiceDto invoiceToInvoiceDto(Invoice invoice);

    @Mapping(target="invoiceId", source="invoice.invoiceId")
    @Mapping(target="productId", source="product.productId")
    public abstract InvoiceItemDto invoiceItemToInvoiceItemDto(InvoiceItem invoiceItem);


    public abstract List<Invoice> invoiceDtoToInvoice(List<InvoiceDto> invoiceDtos);

    public abstract List<InvoiceDto> invoiceToInvoiceDto(List<Invoice> invoices);
}
