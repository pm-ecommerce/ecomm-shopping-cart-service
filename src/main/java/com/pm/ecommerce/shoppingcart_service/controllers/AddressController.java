package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.ApiResponse;
import com.pm.ecommerce.entities.User;
import com.pm.ecommerce.shoppingcart_service.entities.CartItemResponse;
import com.pm.ecommerce.shoppingcart_service.services.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/addresses")
public class AddressController {

    private IAddressService addressService;

    @Autowired
    public AddressController(IAddressService addressService){
        this.addressService = addressService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Address>> registerAddress(@RequestBody Address postData, @PathVariable Integer userId) throws Exception {
        ApiResponse<Address> response = new ApiResponse<>();
        try {
            Address address = addressService.saveAddress(postData,userId);
            response.setData(address);
            response.setMessage("Address registered successfully.");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Address>>> getAllAddress(@PathVariable Integer userId){
        ApiResponse<List<Address>> response = new ApiResponse<>();
        try{
            List<Address> addresses = addressService.getAllAddresses(userId);
            response.setData(addresses);
            response.setMessage("List of Addresses");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> updateAddress(@RequestBody Address updateAddress,@PathVariable Integer addressId){
        ApiResponse<Address> response = new ApiResponse<>();
        try {
            Address address = addressService.updateAddress(updateAddress,addressId);
            response.setData(address);
            response.setMessage("Address registered successfully.");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/{addressId}")
    public ResponseEntity<ApiResponse<Address>> deleteAddress(@PathVariable int userId, @PathVariable int addressId){
        ApiResponse<Address> response = new ApiResponse<>();

        try {
            Address address =  addressService.deleteAddress(userId,addressId);
            response.setData(address);
            response.setMessage("Deleted address id - " + addressId);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
