package com.pm.ecommerce.shoppingcart_service.repositories;

import com.pm.ecommerce.entities.Cart;
import com.pm.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findBySessionId(String sessionId);
    Optional<Cart> findByUserId(int userId);
}
