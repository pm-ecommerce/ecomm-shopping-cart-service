package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.enums.CardType;
import lombok.Data;

@Data
public class CardResponse {
    int id;
    int expMonth;
    int expYear;
    int last4;
    CardType brand;

    public CardResponse(Card card) {
        id = card.getId();
        expMonth= card.getExpMonth();
        expYear = card.getExpYear();
        last4 = card.getLast4();
        brand=card.getBrand();

    }
}
