package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.Cart;
import com.pm.ecommerce.shoppingcart_service.interfaces.ICartService;
import com.pm.ecommerce.shoppingcart_service.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService{

    @Autowired
    CartRepository cartRepository;

    @Override
    public Address addProduct(Address address) {
        return cartRepository.save(address);
    }
}
