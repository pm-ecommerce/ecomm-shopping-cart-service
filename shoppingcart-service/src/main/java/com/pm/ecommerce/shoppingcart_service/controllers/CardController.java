package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.exceptions.PostDataValidationException;
import com.pm.ecommerce.shoppingcart_service.repositories.AccountRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CardRepository;
import com.pm.ecommerce.shoppingcart_service.services.IAccountService;
import com.pm.ecommerce.shoppingcart_service.services.ICardService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/card")
public class CardController {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ICardService cardService;

    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Card>> addCard(@RequestBody CardRequest card, @PathVariable Integer accountId)
            throws PostDataValidationException {
        ApiResponse<Card> response = new ApiResponse<>();
        try{
            Card card1 = cardService.addCard(card, accountId);
            response.setData(card1);
        } catch (Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<ApiResponse<String>> deleteCard(@PathVariable Integer cardId){
        ApiResponse<String> response = new ApiResponse<>();
        try{
            Card card = cardService.deleteCard(cardId);
            response.setData("Card Successfully Deleted");
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("{cardId}/{amount}")
    public ResponseEntity<ApiResponse<Transaction>> chargeCard(@PathVariable int cardId, @PathVariable double amount)
            throws PostDataValidationException{
        ApiResponse<Transaction> response = new ApiResponse<>();
        try{
            Stripe.apiKey = "sk_test_51HfWSjLIEKOZlioqUhvNCD8NsAHgoe7zKES0ki8JYwXATcBuHUDgw8XTV96TmrFN8Z0IJnvrJ9pttxOZbaYboA2T00yp29ot3E";

            Card card = cardService.getCardById(cardId);
            if(card == null) throw new PostDataValidationException("Card Is Not valid");
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "usd");
            params.put("customer", card.getCustomerId());
            params.put(
                    "description",
                    "My First Test Charge (created for API docs)"
            );

            Charge charge = Charge.create(params);
            StripeTransaction transaction = new StripeTransaction();
            transaction.setCard(card);
            transaction.setAmount(amount);
//            transaction.setChargeId(charge.getId());

            response.setData(transaction);

        } catch(StripeException e){
            response.setStatus(400);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }




}
