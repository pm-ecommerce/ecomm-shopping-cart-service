package com.pm.ecommerce.shoppingcart_service.entities;

import lombok.Data;

@Data
public class ChargeRequest {
    public enum Currency {
        USD, EUR;
    }

    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;

}
