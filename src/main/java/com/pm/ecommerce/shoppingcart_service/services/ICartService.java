package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Cart;
import com.pm.ecommerce.entities.CartItem;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemResponse;
import com.pm.ecommerce.shoppingcart_service.entities.CartResponse;

import java.util.List;

public interface ICartService {
    List<CartItemResponse> getCartItems(String sessionId) throws Exception;

    CartItemResponse addToCart(CartItemRequest item, String sessionId) throws Exception;

    CartItemResponse updateCartItem(CartItemRequest item, String sessionId) throws Exception;

    CartItemResponse deleteCartItem(int cartItemId, String sessionId) throws Exception;

    CartResponse initiateCart();
}
