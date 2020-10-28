package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.Cart;
import lombok.Data;

@Data
public class CartResponse {
    private int id;
    private String sessionId;

    public CartResponse(Cart cart){
        this.id = cart.getId();
        this.sessionId = cart.getSessionId();
    }

}
