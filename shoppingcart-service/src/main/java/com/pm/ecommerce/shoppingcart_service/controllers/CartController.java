package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.ApiResponse;
import com.pm.ecommerce.entities.Cart;
import com.pm.ecommerce.entities.CartItem;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @GetMapping("{userId}")
    public ResponseEntity<ApiResponse<Cart>> viewCart(@PathVariable int userId){
        ApiResponse<Cart> response = new ApiResponse<>();
        try{
            Cart cart = cartService.getCart(userId);
            response.setData(cart);
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("{cartId}")
    public ResponseEntity<ApiResponse<Cart>> addProduct(@RequestBody CartItem item, @PathVariable int cartId) {
        ApiResponse<Cart> response = new ApiResponse<>();
        try {
            Cart cart = cartService.addProduct(item, cartId);
            response.setData(cart);
            response.setMessage("Cart registered successfully.");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("{cartId}")
    public ResponseEntity<ApiResponse<Cart>> updateProduct(@RequestBody CartItem item, @PathVariable int cartId){
        ApiResponse<Cart> response = new ApiResponse<>();
        try{
            Cart cart = cartService.updateProduct(item, cartId);
            response.setData(cart);
            response.setMessage("Cart Items updated");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{cartId}")
    public ResponseEntity<ApiResponse<Cart>> deleteProduct(@RequestBody CartItem item, @PathVariable int cartId){
        ApiResponse<Cart> response = new ApiResponse<>();
        try{
            Cart cart = cartService.deleteProduct(item, cartId);
            response.setData(cart);
            response.setMessage("Cart Items updated");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

}
