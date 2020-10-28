package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.CartItem;
import lombok.Data;

@Data
public class CartItemResponse {
    private int id;
    private int quantity;

    public CartItemResponse(CartItem cartItem){
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
    }
}
