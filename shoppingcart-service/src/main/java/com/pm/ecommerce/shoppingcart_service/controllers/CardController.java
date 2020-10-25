package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.shoppingcart_service.exceptions.PostDataValidationException;
import com.pm.ecommerce.shoppingcart_service.repositories.AccountRepository;
import com.pm.ecommerce.shoppingcart_service.repositories.CardRepository;
import com.pm.ecommerce.shoppingcart_service.services.IAccountService;
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

    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Card>> addCard(@RequestBody Card card, @PathVariable Integer accountId)
            throws PostDataValidationException {
        ApiResponse<Card> response = new ApiResponse<>();
        try{
            Stripe.apiKey = "sk_test_51HfWSjLIEKOZlioqUhvNCD8NsAHgoe7zKES0ki8JYwXATcBuHUDgw8XTV96TmrFN8Z0IJnvrJ9pttxOZbaYboA2T00yp29ot3E";
            Account account = accountService.findById(accountId);
            if(account == null) throw new PostDataValidationException("Account Not Found");

            Customer customer = Customer.retrieve(card.getCustomerId());
            if(customer == null) throw new PostDataValidationException("Card is Not Valid");

            card.setUser(account);
            cardRepository.save(card);
            response.setData(card);
        } catch (Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("{cardId}/{amount}")
    public ResponseEntity<ApiResponse<Transaction>> chargeCard(@PathVariable String cardId, @PathVariable int amount)
            throws PostDataValidationException{
        ApiResponse<Transaction> response = new ApiResponse<>();
        try{
            Stripe.apiKey = "sk_test_51HfWSjLIEKOZlioqUhvNCD8NsAHgoe7zKES0ki8JYwXATcBuHUDgw8XTV96TmrFN8Z0IJnvrJ9pttxOZbaYboA2T00yp29ot3E";

            Card card = cardRepository.findByCardId(cardId);
            if(card == null) throw new PostDataValidationException("Card Is Not valid");
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "usd");
            params.put("source", card.getToken());
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
