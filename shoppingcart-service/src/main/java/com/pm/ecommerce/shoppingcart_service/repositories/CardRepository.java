package com.pm.ecommerce.shoppingcart_service.repositories;

import com.pm.ecommerce.entities.Account;
import com.pm.ecommerce.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findAllByUser(Account account);
}
