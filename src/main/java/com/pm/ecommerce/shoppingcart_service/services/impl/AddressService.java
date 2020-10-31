package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.shoppingcart_service.repositories.AddressRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.UserRepository;
import com.pm.ecommerce.shoppingcart_service.services.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public Address registerAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address findById(int addressId) {
        Optional<Address> result = addressRepository.findById(addressId);

        Address address = new Address();
        if(result.isPresent()) {
            address = result.get();
        } else {
            throw new RuntimeException("Did not find by address id - " + addressId);
        }
        return address;
    }

    @Override
    public void deleteById(int addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public void saveAddressByUserId(Integer addressId, Integer userId) {
        userRepository.saveAddressByUserId(addressId, userId);
    }

}
