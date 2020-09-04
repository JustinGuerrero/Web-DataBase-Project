package edu.montana.csci.csci440.model;

import java.util.Collections;
import java.util.List;

public class Invoice extends Model {
    public List<InvoiceItem> getInvoiceItems(){
        return Collections.emptyList();
    }
    public Customer getCustomer() {
        return null;
    }
}
