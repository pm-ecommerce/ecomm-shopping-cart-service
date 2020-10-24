package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.Account;
import com.pm.ecommerce.shoppingcart_service.repositories.AccountRepository;
import com.pm.ecommerce.shoppingcart_service.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account findById(Integer accountId) {
        return accountRepository.findById(accountId).get();
    }
}
