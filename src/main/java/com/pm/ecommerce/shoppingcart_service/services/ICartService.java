package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Cart;
import com.pm.ecommerce.entities.CartItem;

public interface ICartService {
    Cart getCart(String sessionId) throws Exception;

    Cart addProduct(CartItem item, String sessionId) throws Exception;

    Cart updateProduct(CartItem item, String sessionId) throws Exception;

    Cart deleteProduct(CartItem item, String sessionId) throws Exception;

    Cart initCart();
}
