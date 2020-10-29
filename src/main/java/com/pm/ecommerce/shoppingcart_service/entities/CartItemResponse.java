package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.CartItem;
import com.pm.ecommerce.entities.CartItemAttribute;
import lombok.Data;

import java.util.Set;

@Data
public class CartItemResponse {
    private int id;
    private int quantity;
    private String name;
    private Set<CartItemAttribute> cartItemAttributes;

    public CartItemResponse(CartItem cartItem){
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.name = cartItem.getProduct().getName();
        this.cartItemAttributes = cartItem.getAttributes();
    }

//    public CartItemResponse(CartItemRequest request){
//        this.id = request.getId();
//        this.quantity = request.getQuantity();
//        this.name = request.getProduct().getName();
//        this.cartItemAttributes = request.getAttributes();
//    }
}
