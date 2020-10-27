package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Cart;
import com.pm.ecommerce.entities.CartItem;

public interface ICartService {
    Cart getCart(int userId) throws Exception;

    Cart addProduct(CartItem item, int cartId) throws Exception;

    Cart updateProduct(CartItem item, int cartId) throws Exception;

    Cart deleteProduct(CartItem item, int cartId) throws Exception;
}
