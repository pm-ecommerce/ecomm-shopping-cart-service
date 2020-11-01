package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.CartItem;
import com.pm.ecommerce.entities.CartItemAttribute;
import com.pm.ecommerce.entities.Image;
import lombok.Data;

import java.util.Set;

@Data
public class CartItemResponse {
    private int id;
    private int userId;
    private int quantity;
    private double rate;
    private String name;
    private Set<CartItemAttribute> cartItemAttributes;
    private Image image;

    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.rate = cartItem.getRate();
        this.name = cartItem.getProduct().getName();
        this.cartItemAttributes = cartItem.getAttributes();
        this.image = cartItem.getProduct().getImages().stream().findFirst().get();
    }

}
