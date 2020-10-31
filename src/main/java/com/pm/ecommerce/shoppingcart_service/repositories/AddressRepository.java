package com.pm.ecommerce.shoppingcart_service.repositories;

import com.pm.ecommerce.entities.Account;
import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAllById(Integer addressId);
}
