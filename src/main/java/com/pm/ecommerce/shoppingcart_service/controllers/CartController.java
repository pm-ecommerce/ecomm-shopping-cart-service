package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.ApiResponse;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemResponse;
import com.pm.ecommerce.shoppingcart_service.entities.CartResponse;
import com.pm.ecommerce.shoppingcart_service.entities.UserRequest;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/cart")
public class CartController {
    private final ICartService cartService;

    @Autowired
    public CartController(ICartService cartService){
        this.cartService = cartService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CartResponse>> initiateCart(@RequestBody UserRequest userRequest){
        ApiResponse<CartResponse> response = new ApiResponse<>();
        try{
            CartResponse cartResponse;
            if (userRequest == null || userRequest.getUserId() == null) {
                cartResponse = cartService.initiateCart();
            } else {
                cartResponse = cartService.initiateCart(userRequest.getUserId());
            }
            response.setData(cartResponse);
            response.setMessage("Cart successfully generated");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{sessionId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateUserSession(
            @RequestBody UserRequest userRequest, @PathVariable(name = "sessionId") String sessionId){
        ApiResponse<CartResponse> response = new ApiResponse<>();
        try{
            CartResponse cartResponse = cartService.updateUserSession(userRequest.getUserId(), sessionId);
            response.setData(cartResponse);
            response.setMessage("User's Cart Item Successfully updated with Guest's cart item");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("{sessionId}")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> viewCartItems(@PathVariable(name = "sessionId") String sessionId){
        ApiResponse<List<CartItemResponse>> response = new ApiResponse<>();
        try{
            List<CartItemResponse> cartItems = cartService.getCartItems(sessionId);
            response.setData(cartItems);
            response.setMessage("List of Cart Items");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("{sessionId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(@RequestBody CartItemRequest item, @PathVariable String sessionId) {
        ApiResponse<CartItemResponse> response = new ApiResponse<>();
        try {
            CartItemResponse cartResponse = cartService.addToCart(item, sessionId);
            response.setData(cartResponse);
            response.setMessage("Cart Items added!");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{sessionId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItems(@RequestBody CartItemRequest item, @PathVariable String sessionId){
        ApiResponse<CartItemResponse> response = new ApiResponse<>();
        try{
            CartItemResponse cartResponse = cartService.updateCartItem(item, sessionId);
            response.setData(cartResponse);
            response.setMessage("Cart Items updated!");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{sessionId}/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> deleteCartItem(@PathVariable String sessionId, @PathVariable int cartItemId){
        ApiResponse<CartItemResponse> response = new ApiResponse<>();
        try{
            CartItemResponse carItemResponse = cartService.deleteCartItem(cartItemId, sessionId);
            response.setData(carItemResponse);
            response.setMessage("Cart Item deleted!");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

}
