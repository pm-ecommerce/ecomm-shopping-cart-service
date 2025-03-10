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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Cart cart = new Cart();
        if (userId > 0) {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                throw new Exception("User ID not Valid!");
            }
            cart.setUser(user);
        }

        Cart userCart = cartRepository.findByUserId(userId).orElse(null);
        if (userCart != null) {
            return new CartResponse(userCart, userId);
        }

        String uniqueid = "";
        while (true) {
            uniqueid = UUID.randomUUID().toString();
            Cart cartBySessionId = cartRepository.findBySessionId(uniqueid).orElse(null);
            if (cartBySessionId == null) {
                break;
            }
        }

        cart.setSessionId(uniqueid);
        return new CartResponse(cartRepository.save(cart), userId);
    }

    @Override
    public CartResponse updateUserSession(int userId, String sessionId) throws Exception {
        Cart guestCart = cartRepository.findBySessionId(sessionId).orElse(null);
        if (guestCart == null) {
            throw new Exception("Cart Not Found");
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new Exception("User ID not Valid!");
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null) {
            Set<CartItem> cartItems = cart.getCartItems();
            if (cartItems != null && cartItems.size() > 0) {
                for (CartItem i : cartItems) {
                    CartItem cartItem = new CartItem();
                    cartItem.setQuantity(i.getQuantity());
                    cartItem.setRate(i.getRate());
                    cartItem.setProduct(i.getProduct());
                    Set<CartItemAttribute> attributes = new HashSet<>();
                    if (i.getAttributes() != null && i.getAttributes().size() > 0) {
                        for (CartItemAttribute a : i.getAttributes()) {
                            CartItemAttribute attribute = new CartItemAttribute();
                            attribute.setName(a.getName());
                            Option option = new Option();
                            option.setName(a.getOption().getName());
                            option.setPrice(a.getOption().getPrice());
                            attribute.setOption(option);
                            attributes.add(attribute);
                        }
                    }
                    cartItem.setAttributes(attributes);
                    guestCart.getCartItems().add(cartItem);
                }
            }
        }

        guestCart.setUser(user);
        cartRepository.save(guestCart);

        if (cart != null) {
            cartRepository.delete(cart);
        }

        return new CartResponse(guestCart, userId);
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
        return cart.getCartItems().stream().map(CartItemResponse::new).collect(Collectors.toList());
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

        return new CartItemResponse(cartItem);
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
        return new CartItemResponse(cartItem);
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
        return new CartItemResponse(item);
    }

}
