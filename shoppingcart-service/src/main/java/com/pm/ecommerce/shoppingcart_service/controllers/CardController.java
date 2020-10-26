package com.pm.ecommerce.shoppingcart_service.controllers;

import com.pm.ecommerce.entities.*;
import com.pm.ecommerce.shoppingcart_service.entities.CardRequest;
import com.pm.ecommerce.shoppingcart_service.entities.CardResponse;
import com.pm.ecommerce.shoppingcart_service.entities.TransactionResponse;
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
@CrossOrigin
@RequestMapping("/api/card")
public class CardController {
    @Autowired
    private ICardService cardService;

    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponse<CardResponse>> addCard(@RequestBody CardRequest cardRequest, @PathVariable Integer accountId) {
        ApiResponse<CardResponse> response = new ApiResponse<>();
        try{
//            Account account = accountService.getAccount(accountId);
//            if(account == null) throw new PostDataValidationException("Account Not Found");
            CardResponse card = cardService.addCard(cardRequest, accountId);
            response.setMessage("Card Successfully Added");
            response.setData(card);
        } catch (Exception e){
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<ApiResponse<Card>> deleteCard(@PathVariable int cardId){
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

    @GetMapping("{userId}")
    public ResponseEntity<ApiResponse<List<Card>>> getCards(@PathVariable int userId) throws Exception{
        ApiResponse<List<Card>> response = new ApiResponse<>();
        try{
            List<Card> cards = cardService.getUserCards(userId);
            response.setData(cards);
            response.setMessage("Card List");

        }catch(Exception e){
            response.setStatus(500);
            response.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("{accountId}/{cardId}/{amount}")
    public ResponseEntity<ApiResponse<TransactionResponse>> chargeCard(@PathVariable int accountId, @PathVariable int cardId, @PathVariable double amount)
            throws PostDataValidationException{
        ApiResponse<TransactionResponse> response = new ApiResponse<>();
        try{
            TransactionResponse transaction = cardService.chargeCard(accountId, cardId, amount);
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
