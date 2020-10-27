package com.pm.ecommerce.shoppingcart_service.repositories;

import com.pm.ecommerce.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
