package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.entities.Transaction;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CardResponse;
import com.pm.ecommerce.shoppingcart_service.entities.TransactionResponse;
import com.pm.ecommerce.shoppingcart_service.exceptions.PostDataValidationException;
import com.stripe.exception.StripeException;

import java.util.List;

public interface ICardService {
    Card deleteCard(int cardId);

    Card getCardById(int id);

    CardResponse addCard(CardRequest card, int accountId) throws Exception;

    List<Card> getUserCards(int userId) throws Exception;

    TransactionResponse chargeCard(int accountId, int cardId, double amount) throws Exception;
}
