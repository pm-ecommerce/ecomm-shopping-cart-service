package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.Account;
import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.entities.StripeTransaction;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CardResponse;
import com.pm.ecommerce.shoppingcart_service.entities.TransactionResponse;
import com.pm.ecommerce.shoppingcart_service.repositories.AccountRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CardRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.TransactionRepository;
import com.pm.ecommerce.shoppingcart_service.services.ICardService;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CardService implements ICardService {
    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    public CardService(CardRepository cardRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public CardResponse addCard(CardRequest cardRequest, int accountId) throws Exception {
        Stripe.apiKey = "sk_test_I8Ora3L8Af2oo9fgBykDOAxj";
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) throw new Exception("Account Not Found");

        Map<String, Object> params = new HashMap<>();
        params.put("name", account.getName());
        params.put("email", account.getEmail());
        params.put("source", cardRequest.getToken());
        Customer customer = Customer.create(params);
        //if customer is not valid, stripe will throw error or return null
        Card card = cardRequest.toCard();
        card.setUser(account);
        card.setCustomerId(customer.getId());

        return new CardResponse(cardRepository.save(card));
    }

    @Override
    public List<CardResponse> getUserCards(int userId) throws Exception {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) throw new Exception("User Not Found");
        return cardRepository.findAllByUser(account).stream().map(CardResponse::new).collect(Collectors.toList());
    }

    @Override
    public TransactionResponse chargeCard(int accountId, int cardId, double amount) throws Exception {
        Stripe.apiKey = "sk_test_I8Ora3L8Af2oo9fgBykDOAxj";

        Account account = accountRepository.findById(accountId).orElse(null);

        if (account == null) throw new Exception("Account not found");

        Card card = cardRepository.findById(cardId).orElse(null);
        if (card == null) throw new Exception("Card not found");

        if (card.getUser().getId() != account.getId()) throw new Exception("This card does not belong to this account");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) amount * 100);
        params.put("currency", "usd");
        params.put("customer", card.getCustomerId());
        params.put("description", "My First Test Charge (created for API docs)");

        Charge charge = Charge.create(params);
        StripeTransaction transaction = new StripeTransaction();
        transaction.setCard(card);
        transaction.setAmount(amount);

        transaction.setChargeId(charge.getId());
        transactionRepository.save(transaction);
        return new TransactionResponse(transaction);
    }

    public Card getCardById(int id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public CardResponse deleteCard(int cardId) {
        Card card = getCardById(cardId);
        cardRepository.delete(card);
        return new CardResponse(card);
    }
}
