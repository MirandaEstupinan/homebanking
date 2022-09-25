package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard(
            @RequestParam CardColor cardColor, @RequestParam CardType cardType, @RequestParam String accountOrigin,
            Authentication authentication){

        Client client = clientService.getClientByEmail(authentication.getName());
        String cardNumber = CardUtils.getCardNumber();
        int cvv = CardUtils.getCvv();
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        Account account = accountService.findAccountByNumber(accountOrigin);



        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("Missing data2", HttpStatus.FORBIDDEN);
        }

        if(!account.getNumber().contains(accountOrigin)){
            return new ResponseEntity<>("Missing account", HttpStatus.FORBIDDEN);
        }
/*        if (client.getCards().stream().filter(card -> card.getCardType().equals(cardType) && card.getActive().equals(true)).count() >= 3){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }*/

        if(client.getCards().stream().anyMatch(card -> card.getCardColor().equals(cardColor) && card.getCardType().equals(cardType) && card.getActive().equals(true))){
            return new ResponseEntity<>("Cannot have more than 3 cards of the same type or color", HttpStatus.FORBIDDEN);
        }

        cardService.saveCard(new Card(cardHolder, cardNumber, cvv,LocalDate.now().plusYears(5), LocalDate.now(), cardColor,cardType, client, true, account));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @PatchMapping("/clients/current/cards/delete")
    public ResponseEntity<Object> deleteCard(@RequestParam Long CardId,  Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        Card card = cardService.getCardById(CardId);

        if(card == null){
            return new ResponseEntity<>("the card does not exist", HttpStatus.FORBIDDEN);
        }

        if(client == null){
            return new ResponseEntity<>("Client does not exist", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("Client does not exist", HttpStatus.FORBIDDEN);
        }
        cardService.deleteCard(card);
        return new ResponseEntity<>("Card deleted successfully", HttpStatus.ACCEPTED);
    }




}
