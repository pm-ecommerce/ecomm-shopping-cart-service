package com.pm.ecommerce.shoppingcart_service.entities;

import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.enums.CardType;
import lombok.Data;

@Data
public class CardRequest {
    private int id;
    private String token;
    private String brand;
    private int last4;
    private String customerId;
    private int expMonth;
    private int expYear;
    private String cardId;

    public Card toCard(){
        Card card = new Card();
        card.setId(id);
        card.setLast4(last4);
        card.setToken(token);
        card.setCustomerId(customerId);
        card.setExpMonth(expMonth);
        card.setExpYear(expYear);
        card.setCardId(cardId);
        card.setBrand(getCardBrand());
        return card;
    }

    private CardType getCardBrand() {
        if(brand.toLowerCase().equals("visa")){
            return CardType.Visa;
        }
        return CardType.Mastercard;
    }
}