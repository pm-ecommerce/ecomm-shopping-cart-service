package com.pm.ecommerce.shoppingcart_service.repositories;

import com.pm.ecommerce.entities.Account;
import com.pm.ecommerce.entities.Address;
import com.pm.ecommerce.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUser(Account user);

    Optional<Cart> findBySessionId(String sessionId);
}
