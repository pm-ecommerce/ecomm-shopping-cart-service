package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Address;

import java.util.List;

public interface IAddressService {

    Address saveAddress(Address address, Integer userId) throws Exception;

    List<Address> getAllAddresses();

    Address findById(int addressId);

    void deleteById(int addressId);

    Address updateAddress(Address address, Integer addressId) throws Exception;

    Address deleteAddress(Integer userId, Integer addressId) throws Exception;
}
