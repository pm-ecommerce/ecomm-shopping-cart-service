package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Address;

import java.util.List;

public interface IAddressService {

    Address registerAddress(Address address);

    List<Address> getAllAddresses();

    Address findById(int addressId);

    void deleteById(int addressId);
}
