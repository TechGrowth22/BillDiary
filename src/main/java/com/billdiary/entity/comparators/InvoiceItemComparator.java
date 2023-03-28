package com.billdiary.entity.comparators;


import com.billdiary.entity.InvoiceItem;

import java.util.Comparator;

public class InvoiceItemComparator implements Comparator<InvoiceItem> {
    @Override
    public int compare(InvoiceItem invoiceOne, InvoiceItem invoiceTwo) {
        if (invoiceOne == null && invoiceTwo == null) {
            return 0;
        }
        if (invoiceOne == null) {
            return -1;
        }
        if (invoiceTwo == null) {
            return 1;
        }
        return Long.compare(invoiceOne.getInvoiceItemId(), invoiceTwo.getInvoiceItemId());
    }
}
