package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.ApiResponse;
import com.pm.ecommerce.entities.Card;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CardResponse;
import com.pm.ecommerce.shoppingcart_service.entities.TransactionResponse;
import com.pm.ecommerce.shoppingcart_service.services.ICardService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/card")
public class CardController {
    private final ICardService cardService;

    @Autowired
    public CardController(ICardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<CardResponse>> addCard(@RequestBody CardRequest cardRequest, @PathVariable Integer accountId) {
        ApiResponse<CardResponse> response = new ApiResponse<>();
        try {
            CardResponse card = cardService.addCard(cardRequest, accountId);
            response.setMessage("Card Successfully Added");
            response.setData(card);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<ApiResponse<CardResponse>> deleteCard(@PathVariable int cardId) {
        ApiResponse<CardResponse> response = new ApiResponse<>();
        try {
            CardResponse card = cardService.deleteCard(cardId);
            response.setMessage("Card Successfully Deleted");
            response.setData(card);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("{userId}")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getCards(@PathVariable int userId) {
        ApiResponse<List<CardResponse>> response = new ApiResponse<>();
        try {
            List<CardResponse> cards = cardService.getUserCards(userId);
            response.setData(cards);
            response.setMessage("Card List");

        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("{accountId}/{cardId}/{amount}")
    public ResponseEntity<ApiResponse<TransactionResponse>> chargeCard(@PathVariable int accountId, @PathVariable int cardId, @PathVariable double amount) {
        ApiResponse<TransactionResponse> response = new ApiResponse<>();
        try {
            TransactionResponse transaction = cardService.chargeCard(accountId, cardId, amount);
            response.setData(transaction);

        } catch (StripeException e) {
            response.setStatus(400);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }


}
