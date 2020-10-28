package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.shoppingcart_service.repositories.AccountRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CartRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CartItemRepository;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;


    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository){
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart initCart() {
        Cart cart = new Cart();
        Set<String> sessionIdSets = cartRepository.findAll().stream().map(o -> o.getSessionId()).collect(Collectors.toSet());
        // SessionId has to be Unique -> thus first check before
        String currentSessionId = "";
        boolean flag = true;
        while(flag){
            currentSessionId = UUID.randomUUID().toString();
            if(sessionIdSets.add(currentSessionId)){
                flag = false;
            }
        }
        cart.setSessionId(currentSessionId);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCart(String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Valid");
        return cartRepository.save(cart);
    }

    @Override
    public Cart addProduct(CartItem item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        Set<CartItem> cartItems = cart.getCartItems();
        if(cartItems == null) {
            cartItems = new HashSet<>();
        }
        cartItems.add(item);
        cart.setCartItems(cartItems);
        return cartRepository.save(cart);
    }

    public Cart updateProduct(CartItem item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        Set<CartItem> cartItems = cart.getCartItems();
        for(CartItem i : cartItems){
            if(i.equals(item)){
                i.setQuantity(i.getQuantity() + item.getQuantity());
            }
        }
        cart.setCartItems(cartItems);
        cartItemRepository.saveAll(cartItems);
        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteProduct(CartItem item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        Set<CartItem> cartItems = cart.getCartItems();
        cartItems.remove(item);
        cart.setCartItems(cartItems);
        return cartRepository.save(cart);
    }

}
