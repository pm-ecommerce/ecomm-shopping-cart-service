package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.enums.ProductStatus;
import com.pm.ecommerce.enums.VendorStatus;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemResponse;
import com.pm.ecommerce.shoppingcart_service.entities.CartResponse;
import com.pm.ecommerce.shoppingcart_service.repositories.CartRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.ProductRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.UserRepository;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartResponse initiateCart(int userId) throws Exception {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null ){
            throw new Exception("User ID not Valid!");
        }
        String uniqueid = "";
        while (true) {
            uniqueid = UUID.randomUUID().toString();
            Cart cart = cartRepository.findBySessionId(uniqueid).orElse(null);
            if (cart == null) {
                break;
            }
        }
        Cart cart = new Cart();
        cart.setSessionId(uniqueid);
        cart.setUser(user);
        return new CartResponse(cartRepository.save(cart), user.getId());
    }

    @Override
    public CartResponse initiateCart() {
        String uniqueid = "";
        while (true) {
            uniqueid = UUID.randomUUID().toString();
            Cart cart = cartRepository.findBySessionId(uniqueid).orElse(null);
            if (cart == null) {
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
        if (cart == null) throw new Exception("Cart Not Valid");
        List<CartItemResponse> cartItems = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            CartItemResponse cartItemResponse = new CartItemResponse(item);
//            cartItemResponse.setUserId(cart.getUser().getId());
            cartItems.add(cartItemResponse);
        }
        return cartItems;
    }

    @Override
    public CartItemResponse addToCart(CartItemRequest item, String sessionId) throws Exception {
        if (item == null) {
            throw new Exception("Item should be not null");
        }

        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if (cart == null) {
            throw new Exception("Cart Not Found");
        }

        //Checking Product validation
        Product product = productRepository.findById(item.getProductId()).orElse(null);
        if (product == null || product.getStatus() != ProductStatus.PUBLISHED) {
            throw new Exception("Product not Valid");
        }

        //Checking Vendor status
        Vendor vendor = product.getVendor();
        if (vendor == null || vendor.getStatus() != VendorStatus.APPROVED) {
            throw new Exception("Vendor status not Approved");
        }

        //Checking Vendor status
        Category category = product.getCategory();
        if (category == null || category.isDeleted()) {
            throw new Exception("Category is not available");
        }

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

        // after save
        // get the cart again
        // and then get the cart item
        cart = cartRepository.findById(cart.getId()).orElse(null);

        if (cart != null && cart.getCartItems() != null) {
            for (CartItem item1 : cart.getCartItems()) {
                if (item1.getId() > cartItem.getId()) {
                    cartItem = item1;
                }
            }
        }
        //If Guest User -> it will return No userId
        User user = cart.getUser();
        if(user == null){
            return new CartItemResponse(cartItem);
        }
        return new CartItemResponse(cartItem, user.getId());
    }

    public CartItemResponse updateCartItem(CartItemRequest item, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if (cart == null) {
            throw new Exception("Cart Not Found");
        }

        CartItem cartItem = cart.getCartItems().stream().reduce(null, (a, b) -> b.getId() == item.getId() ? b : a);
        if (cartItem == null) {
            throw new Exception("Cart Item Not Found");
        }
        //Checking Product validation
        Product product = productRepository.findById(cartItem.getProduct().getId()).orElse(null);
        if (product == null || product.getStatus() != ProductStatus.PUBLISHED) {
            // delete cart item from cart
            deleteCartItem(item.getId(), sessionId);
        }

        //Checking Vendor status
        Vendor vendor = product.getVendor();
        if (vendor == null || vendor.getStatus() != VendorStatus.APPROVED) {
            // delete cart item from cart
            deleteCartItem(item.getId(), sessionId);
        }

        //Checking Vendor status
        Category category = product.getCategory();
        if (category == null || category.isDeleted()) {
            // delete cart item from cart
            deleteCartItem(item.getId(), sessionId);
        }

        cartItem.setQuantity(item.getQuantity());

        cartRepository.save(cart);
        //If Guest User it will return No userId
        User user = cart.getUser();
        if(user == null){
            return new CartItemResponse(cartItem);
        }
        return new CartItemResponse(cartItem, user.getId());
    }

    @Override
    public CartItemResponse deleteCartItem(int cartItemId, String sessionId) throws Exception {
        Cart cart = cartRepository.findBySessionId(sessionId).orElse(null);
        if (cart == null) {
            throw new Exception("Cart Not Found");
        }
        //Checking Product validation
        CartItem item = cart.getCartItems().stream().reduce(null, (a, b) -> b.getId() == cartItemId ? b : a);
        if (item == null) {
            throw new Exception("Cart Item Not found");
        }

        Set<CartItem> cartItems = cart.getCartItems();
        cartItems.remove(item);
        cartRepository.save(cart);
        //If Guest User it will return No userId
        User user = cart.getUser();
        if(user == null){
            return new CartItemResponse(item);
        }
        return new CartItemResponse(item, user.getId());
    }

}
