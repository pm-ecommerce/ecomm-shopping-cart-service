package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Account;

public interface IAccountService {
    Account findById(Integer accountId);
}
