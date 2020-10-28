package com.pm.ecommerce.shoppingcart_service.repositories;

import com.pm.ecommerce.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
