package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.entities.Transaction;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.exceptions.PostDataValidationException;
import com.stripe.exception.StripeException;

import java.util.List;

public interface ICardService {
    Card deleteCard(Integer cardId);

    Card getCardById(int id);

    Card addCard(CardRequest card, int accountId) throws Exception;

    List<Card> getCards();

    Transaction chargeCard(int cardId, double amount) throws StripeException;
}
