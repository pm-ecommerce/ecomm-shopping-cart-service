package com.pm.ecommerce.shoppingcart_service.services;

import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.exceptions.PostDataValidationException;

public interface ICardService {
    Card deleteCard(Integer cardId);

    Card getCardById(int id);

    Card addCard(CardRequest card, int accountId) throws Exception;
}
