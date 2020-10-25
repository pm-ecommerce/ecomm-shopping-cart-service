package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.shoppingcart_service.repositories.AccountRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CartRepository;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Cart getCart(int userId) throws Exception {
        Account user = accountRepository.findById(userId).orElse(null);
        if(user == null) throw new Exception("User Not Found");
        return cartRepository.findByUser(user);
    }

    @Override
    public Cart addProduct(CartItem item, int cartId) throws Exception {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) throw new Exception("User and Cart Not Found");
        if(cart.getCartItems() == null) {
            List<CartItem> cartItems = new ArrayList<>();
            cartItems.add(item);
            cart.setCartItems(cartItems);
            return cartRepository.save(cart);
        }
        boolean inCart = cart.getCartItems().stream().equals(item);
        if(!inCart){
            //This CartItem is not in Cart So taking all cartItems and add the CartItem
            List<CartItem> cartItems = cart.getCartItems();
            cartItems.add(item);
            cart.setCartItems(cartItems);
            return cartRepository.save(cart);
        }
        //That item is already in cart, So calling updateCartItem method
        return updateProduct(item, cartId);
    }

    public Cart updateProduct(CartItem item, int cartId) throws Exception {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        List<CartItem> cartItems = cartRepository.findById(cartId).get().getCartItems();
        for(CartItem i : cartItems){
            if(i.equals(item)){
                i.setQuantity(i.getQuantity() + item.getQuantity());
            }
        }
        cart.setCartItems(cartItems);
        return cartRepository.save(cart);
    }

    @Override
    public Cart deleteProduct(CartItem item, int cartId) throws Exception {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        List<CartItem> cartItems = cartRepository.findById(cartId).get().getCartItems();
        for(CartItem i : cartItems){
            if(i.equals(item)){
                i.setQuantity(i.getQuantity() - item.getQuantity());
            }
        }
        cart.setCartItems(cartItems);
        return cartRepository.save(cart);
    }
}
