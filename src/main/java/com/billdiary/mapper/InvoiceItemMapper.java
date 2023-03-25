package com.billdiary.mapper;

import com.billdiary.dto.InvoiceItemDto;
import com.billdiary.entity.InvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract  class InvoiceItemMapper {


    public abstract InvoiceItem invoiceItemDtoToInvoiceItem(InvoiceItemDto invoiceItemDto);

    @Mapping(target="invoiceId", source="invoice.invoiceId")
    @Mapping(target="productId", source="product.productId")
    public abstract InvoiceItemDto invoiceItemToInvoiceItemDto(InvoiceItem invoiceItem);

    public abstract List<InvoiceItem> invoiceItemDtoToInvoiceItem(List<InvoiceItemDto> invoiceItemDtos);

    public abstract List<InvoiceItemDto> invoiceItemToInvoiceItemDto(List<InvoiceItem> invoiceItems);
}
