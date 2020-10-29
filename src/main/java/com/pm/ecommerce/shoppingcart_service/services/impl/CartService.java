package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.enums.ProductStatus;
import com.pm.ecommerce.enums.VendorStatus;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemResponse;
import com.pm.ecommerce.shoppingcart_service.entities.CartResponse;
import com.pm.ecommerce.shoppingcart_service.repositories.CartRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CartItemRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.ProductRepository;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService implements ICartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;


    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository){
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
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
    public CartItemResponse addToCart(CartItemRequest item, String sessionId) throws Exception {
        if(item == null) throw new Exception("Item should be not null");
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        //Checking Product validation
        Product product = productRepository.findById(item.getProductId()).orElse(null);
        if (product.getStatus() != ProductStatus.PUBLISHED) throw new Exception("Product not Valid");
        //Checking Vendor status
        Vendor vendor = product.getVendor();
        if (vendor.getStatus() != VendorStatus.APPROVED) throw new Exception("Vendor status not Approved");
        //Checking Vendor status
        Category category = product.getCategory();
        if (category.isDeleted()) throw new Exception("Category is not available");
        //Setting CartItem object
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(item.getQuantity());
        cartItem.setRate(item.getRate());
        cartItem.setProduct(product);
        cartItem.setAttributes(item.getAttributes());
        //Adding CartItems
        Set<CartItem> cartItems = cart.getCartItems();
        cartItems.add(cartItem);
        cartRepository.save(cart);
        return new CartItemResponse(cartItemRepository.save(cartItem));
    }

    public CartItemResponse updateCartItem(CartItemRequest item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        CartItem cartItem = cartItemRepository.findById(item.getId()).orElse(null);
        if(cartItem == null) throw new Exception("Cart Item Not Found");
        //Checking Product validation
        Product product = productRepository.findById(item.getProductId()).orElse(null);
        if (product.getStatus() != ProductStatus.PUBLISHED) throw new Exception("Product not Valid");
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
        cartRepository.save(cart);
        return new CartItemResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponse deleteCartItem(int cartItemId, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(cart == null) throw new Exception("Cart Not Found");
        //Checking Product validation
        CartItem item = cartItemRepository.findById(cartItemId).orElse(null);
        if(item == null) throw new Exception("Cart Item Not found");
        Product product = item.getProduct();
        if (product.getStatus() != ProductStatus.PUBLISHED) throw new Exception("Product not Valid");
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
