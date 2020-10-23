package com.pm.ecommerce.shoppingcart_service.interfaces;

import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.Cart;

public interface ICartService {
    public Address addProduct(Address address);
}
