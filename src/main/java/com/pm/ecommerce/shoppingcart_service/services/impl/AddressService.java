package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.User;
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
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address saveAddress(Address address, Integer userId) throws Exception {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (address == null) throw new Exception("Address is empty");
        if (existingUser == null) throw new Exception("User not found");
        List<Address> addressList = existingUser.getAddresses();
        address.setCountry("US");
        addressList.add(address);
        userRepository.save(existingUser);

        existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser != null && existingUser.getAddresses() != null) {
            for (Address item1 : existingUser.getAddresses()) {
                if (item1.getId() > address.getId()) {
                    address = item1;
                }
            }
        }

        return address;
    }

    @Override
    public List<Address> getAllAddresses(Integer userId) throws Exception {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) throw new Exception("User not found");
        return existingUser.getAddresses();
    }

    @Override
    public Address findById(int addressId) {
        Optional<Address> result = addressRepository.findById(addressId);

        Address address = new Address();
        if (result.isPresent()) {
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
    public Address updateAddress(Address newAddress, Integer addressId) throws Exception {
        Address address = addressRepository.findById(addressId).orElse(null);
        if (address == null) throw new Exception("Address does not exist");
        newAddress.setId(addressId);
        return addressRepository.save(newAddress);
    }

    public Address deleteAddress(Integer userId, Integer addressId) throws Exception {
        Address address = addressRepository.findById(addressId).orElse(null);
        if (address == null) throw new Exception("Address does not exist");
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new Exception("User does not exist");
        List<Address> addressList = user.getAddresses();
        addressList.remove(address);
        addressRepository.delete(address);
        user.setAddresses(addressList);
        userRepository.save(user);
        return address;
    }
}
