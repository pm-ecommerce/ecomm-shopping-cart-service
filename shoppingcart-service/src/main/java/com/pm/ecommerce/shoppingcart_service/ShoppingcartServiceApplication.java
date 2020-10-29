package com.pm.ecommerce.shoppingcart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EntityScan(basePackages = {"com.pm.ecommerce.entities"})
@EnableDiscoveryClient
public class ShoppingcartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingcartServiceApplication.class, args);
    }

}
