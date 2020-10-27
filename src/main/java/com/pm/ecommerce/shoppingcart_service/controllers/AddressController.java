package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.ApiResponse;
import com.pm.ecommerce.shoppingcart_service.services.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Address>> registerAddress(@RequestBody Address postData) {
        ApiResponse<Address> response = new ApiResponse<>();

        try {
            Address address = addressService.registerAddress(postData);

            response.setData(address);
            response.setMessage("Address registered successfully.");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> getAddressId(@PathVariable int addressId) {
        ApiResponse<Address> response = new ApiResponse<>();

        try {
            Address address = addressService.findById(addressId);
            address.setId(0);
            response.setData(address);
            response.setMessage("Get address by id");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateAddress")
    public ResponseEntity<ApiResponse<Address>> updateAddress(@RequestBody Address updateAddress){
        ApiResponse<Address> response = new ApiResponse<>();

        try {
            Address address = addressService.registerAddress(updateAddress);

            response.setData(address);
            response.setMessage("Address registered successfully.");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Address>> deleteAddress(@PathVariable int addressId){
        ApiResponse<Address> response = new ApiResponse<>();

        try {
            Address address = addressService.findById(addressId);
            addressService.deleteById(addressId);
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
