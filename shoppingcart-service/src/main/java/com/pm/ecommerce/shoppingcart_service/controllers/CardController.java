package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.exceptions.PostDataValidationException;
import com.pm.ecommerce.shoppingcart_service.services.IAccountService;
import com.pm.ecommerce.shoppingcart_service.services.ICardService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardController {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICardService cardService;

    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Card>> addCard(@RequestBody CardRequest cardRequest, @PathVariable Integer accountId) {
        ApiResponse<Card> response = new ApiResponse<>();
        try{
            Account account = accountService.getAccount(accountId);
            if(account == null) throw new PostDataValidationException("Account Not Found");
            Card card = cardService.addCard(cardRequest, accountId);
            response.setMessage("Card Successfully Added");
            response.setData(card);
        } catch (Exception | PostDataValidationException e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<ApiResponse<Card>> deleteCard(@PathVariable Integer cardId){
        ApiResponse<Card> response = new ApiResponse<>();
        try{
            Card card = cardService.deleteCard(cardId);
            response.setMessage("Card Successfully Deleted");
            response.setData(card);
        } catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Card>>> getCards(){
        ApiResponse<List<Card>> response = new ApiResponse<>();
        List<Card> cards = cardService.getCards();
        response.setData(cards);
        response.setMessage("Card List");
        return ResponseEntity.ok(response);
    }

    @GetMapping("{cardId}/{amount}")
    public ResponseEntity<ApiResponse<Transaction>> chargeCard(@PathVariable int cardId, @PathVariable double amount)
            throws PostDataValidationException{
        ApiResponse<Transaction> response = new ApiResponse<>();
        try{
            Transaction transaction = cardService.chargeCard(cardId, amount);
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
