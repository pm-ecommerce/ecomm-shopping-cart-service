package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Cart;
import com.pm.ecommerce.entities.CartItem;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemResponse;
import com.pm.ecommerce.shoppingcart_service.entities.CartResponse;

import java.util.List;

public interface ICartService {
    List<CartItemResponse> getCartItems(String sessionId) throws Exception;

    CartResponse addToCart(CartItem item, String sessionId) throws Exception;

    CartResponse updateProduct(CartItem item, String sessionId) throws Exception;

    CartItemResponse deleteProduct(CartItem item, String sessionId) throws Exception;

    CartResponse initiateCart();
}
