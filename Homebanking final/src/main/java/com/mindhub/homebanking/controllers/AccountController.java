package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    TransactionService transactionService;



    @GetMapping("/accounts")
    public List<AccountDTO> getAccount(){
        return accountService.getAllAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.getAccountById(id));
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam AccountType accountType) {

        String accountNumber = "VIN-" + getRandomNumber(1, 99999999);

        Client client = clientService.getClientByEmail(authentication.getName());
        if (client.getAccounts().stream().filter(account -> account.getAccountActive()).count() >= 3) {
            return new ResponseEntity<>("You cannot have more than 3 accounts", HttpStatus.FORBIDDEN);
        }

        accountService.saveAccount(new Account(0,  accountNumber, LocalDateTime.now(), client, true, accountType));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number){
        Client client = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.findAccountByNumber(number);
        Set<Transaction> transaction = account.getTransactions();

        if(account == null){
            return new ResponseEntity<>("the Account does not exist", HttpStatus.FORBIDDEN);
        }

        if(client == null){
            return new ResponseEntity<>("Account does not exist", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Account does not exist2", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance() > 0){
            return new ResponseEntity<>("You cannot delete an account with a balance greater than 0", HttpStatus.FORBIDDEN);
        }

        transaction.stream().forEach(transaction1 -> transactionService.deleteTransaction(transaction1));
        accountService.deleteAccount(account);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.ACCEPTED);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
