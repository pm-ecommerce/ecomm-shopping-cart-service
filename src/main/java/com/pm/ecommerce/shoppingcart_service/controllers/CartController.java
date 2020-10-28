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
@CrossOrigin
@RequestMapping("api/cart")
public class CartController {
    private ICartService cartService;

    @Autowired
    public CartController(ICartService cartService){
        this.cartService = cartService;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Cart>> initCart(){
        ApiResponse<Cart> response = new ApiResponse<>();
        try{
            Cart cart = cartService.initCart();
            response.setData(cart);
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("{sessionId}")
    public ResponseEntity<ApiResponse<Cart>> viewCart(@PathVariable(name = "sessionId") String sessionId){
        ApiResponse<Cart> response = new ApiResponse<>();
        try{
            Cart cart = cartService.getCart(sessionId);
            response.setData(cart);
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("{sessionId}")
    public ResponseEntity<ApiResponse<Cart>> addProduct(@RequestBody CartItem item, @PathVariable String sessionId) {
        ApiResponse<Cart> response = new ApiResponse<>();
        try {
            Cart cart = cartService.addProduct(item, sessionId);
            response.setData(cart);
            response.setMessage("Cart registered successfully.");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{sessionId}")
    public ResponseEntity<ApiResponse<Cart>> updateProduct(@RequestBody CartItem item, @PathVariable String sessionId){
        ApiResponse<Cart> response = new ApiResponse<>();
        try{
            Cart cart = cartService.updateProduct(item, sessionId);
            response.setData(cart);
            response.setMessage("Cart Items updated");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{sessionId}")
    public ResponseEntity<ApiResponse<Cart>> deleteProduct(@RequestBody CartItem item, @PathVariable String sessionId){
        ApiResponse<Cart> response = new ApiResponse<>();
        try{
            Cart cart = cartService.deleteProduct(item, sessionId);
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
