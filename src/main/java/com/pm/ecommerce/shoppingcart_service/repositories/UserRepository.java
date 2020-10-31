package com.pm.ecommerce.shoppingcart_service.repositories;

import com.pm.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//   INSERT INTO users_addresses  (user_id, addresses_id) VALUES (21, 12);
    @Query(value = "INSERT INTO users_addresses (user_id, address_id)  VALUES (?2, ?1) ", nativeQuery = true)
    void saveAddressByUserId(Integer addressId, Integer userId);
    //UPDATE `pm_db`.`users_addresses` SET `user_id` = '3', `addresses_id` = '2' WHERE (`addresses_id` = '1');

}