package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.Cart;
import lombok.Data;

@Data
public class CartResponse {
    private int id;
    private String sessionId;
    private int userId;

    public CartResponse(Cart cart){
        this.id = cart.getId();
        this.sessionId = cart.getSessionId();
    }

    public CartResponse(Cart cart, int userId){
        this.id = cart.getId();
        this.sessionId = cart.getSessionId();
        this.userId = userId;
    }

}
