package com.billdiary.dao;

import com.billdiary.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT MAX(invoice.invoiceId) FROM Invoice invoice")
    public Long maxInvoiceId();
}
