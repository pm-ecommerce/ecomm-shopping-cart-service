package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.Transaction;
import lombok.Data;

@Data
public class TransactionResponse {
    int id;
    double amount;

    public TransactionResponse(Transaction transaction) {
        id = transaction.getId();
        amount= transaction.getAmount();
    }
}
