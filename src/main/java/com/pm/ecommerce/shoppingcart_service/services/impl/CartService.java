package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.enums.ProductStatus;
import com.pm.ecommerce.enums.VendorStatus;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemResponse;
import com.pm.ecommerce.shoppingcart_service.entities.CartResponse;
import com.pm.ecommerce.shoppingcart_service.repositories.CartRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CartItemRepository;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public CartResponse initiateCart() {
        // SessionId has to be Unique ->
        String uniqueid = "";
        while(true){
            uniqueid = UUID.randomUUID().toString();
            Cart cart = cartRepository.findBySessionId(uniqueid).orElse(null);
            if(cart == null){
                break;
            }
        }
        Cart cart = new Cart();
        cart.setSessionId(uniqueid);
        return new CartResponse(cartRepository.save(cart));
    }

    @Override
    public List<CartItemResponse> getCartItems(String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Valid");
        List<CartItemResponse> cartItems = new ArrayList<>();
        for(CartItem item : cart.getCartItems()){
            CartItemResponse cartItemResponse = new CartItemResponse(item);
            cartItems.add(cartItemResponse);
        }
        return cartItems;
    }

    @Override
    public CartResponse addToCart(CartItem item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        //Checking Product validation
        Product product = item.getProduct();
        if (product.getStatus() != ProductStatus.PUBLISHED.PUBLISHED) throw new Exception("Product not Valid");
        //Checking Vendor status
        Vendor vendor = product.getVendor();
        if (vendor.getStatus() != VendorStatus.APPROVED) throw new Exception("Vendor status not Approved");
        //Checking Vendor status
        Category category = product.getCategory();
        if (category.isDeleted()) throw new Exception("Category is not available");
        Set<CartItem> cartItems = cart.getCartItems();
        cartItems.add(item);
        return new CartResponse(cartRepository.save(cart));
    }

    public CartResponse updateProduct(CartItem item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        //Checking Product validation
        Product product = item.getProduct();
        if (product.getStatus() != ProductStatus.PUBLISHED.PUBLISHED) throw new Exception("Product not Valid");
        //Checking Vendor status
        Vendor vendor = product.getVendor();
        if (vendor.getStatus() != VendorStatus.APPROVED) throw new Exception("Vendor status not Approved");
        //Checking Vendor status
        Category category = product.getCategory();
        if (category.isDeleted()) throw new Exception("Category is not available");
        Set<CartItem> cartItems = cart.getCartItems();
        for(CartItem i : cartItems){
            if(i.equals(item)){
                i.setQuantity(i.getQuantity() + item.getQuantity());
            }
        }
        return new CartResponse(cartRepository.save(cart));
    }

    @Override
    public CartItemResponse deleteProduct(CartItem item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        //Checking Product validation
        Product product = item.getProduct();
        if (product.getStatus() != ProductStatus.PUBLISHED.PUBLISHED) throw new Exception("Product not Valid");
        //Checking Vendor status
        Vendor vendor = product.getVendor();
        if (vendor.getStatus() != VendorStatus.APPROVED) throw new Exception("Vendor status not Approved");
        //Checking Vendor status
        Category category = product.getCategory();
        if (category.isDeleted()) throw new Exception("Category is not available");
        Set<CartItem> cartItems = cart.getCartItems();
        //Version #1
        cartItems.remove(item);
        cartRepository.save(cart);
        //Version #2
//        cartItemRepository.delete(item));
//        cartRepository.save(cart);
        return new CartItemResponse(item);
    }

}
