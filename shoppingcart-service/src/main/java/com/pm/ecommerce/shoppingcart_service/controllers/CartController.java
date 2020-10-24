package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.ApiResponse;
import com.pm.ecommerce.shoppingcart_service.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @PostMapping("add")
    public ResponseEntity<ApiResponse<Address>> registerCart(@RequestBody Address postData) {
        ApiResponse<Address> response = new ApiResponse<>();
        try {
            Address address = cartService.addProduct(postData);
            response.setData(address);
            response.setMessage("Cart registered successfully.");
//        } catch (PostDataValidationException e) {
//            response.setStatus(400);
//            response.setMessage(e.getMessage());
//
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

}
