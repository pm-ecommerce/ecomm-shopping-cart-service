package com.pm.ecommerce.shoppingcart_service.services.impl;

import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.exceptions.PostDataValidationException;
import com.pm.ecommerce.shoppingcart_service.repositories.CardRepository;
import com.pm.ecommerce.shoppingcart_service.services.ICardService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CardService implements ICardService {
    @Autowired
    private CardRepository cardRepository;

    public Card addCard(CardRequest cardRequest, int accountId) throws Exception {
        Stripe.apiKey = "sk_test_51HfWSjLIEKOZlioqUhvNCD8NsAHgoe7zKES0ki8JYwXATcBuHUDgw8XTV96TmrFN8Z0IJnvrJ9pttxOZbaYboA2T00yp29ot3E";
//            Account account = accountService.findById(accountId);
//            if(account == null) throw new PostDataValidationException("Account Not Found");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "DummyName");
        params.put("email", "dummyemail@gmail.com");
        params.put("source", cardRequest.getToken());
        Customer customer = Customer.create(params);

        //if customer is not valid, stripe will throw error or return null
        Card card1 = cardRequest.toCard();
        card1.setCustomerId(customer.getId());

        return cardRepository.save(card1);
    }
    public Card getCardById(int id){
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card deleteCard(Integer cardId) {
        Card card = getCardById(cardId);
        cardRepository.delete(card);
        return card;
    }
}
