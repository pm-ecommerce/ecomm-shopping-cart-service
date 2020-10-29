package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.CartItemAttribute;
import lombok.Data;

import java.util.Set;

@Data
public class CartItemRequest {
    private int id;
    private int quantity;
    private double rate;
    private Set<CartItemAttribute> attributes;
    private int productId;

}
