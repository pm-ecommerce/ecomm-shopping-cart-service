package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.Transaction;
import lombok.Data;

@Data
public class TransactionResponse {
    private int id;
    private double amount;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.amount= transaction.getAmount();
    }
}
