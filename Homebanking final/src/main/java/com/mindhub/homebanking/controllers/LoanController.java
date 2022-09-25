package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @GetMapping ("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/clients/current/loans")
    public ResponseEntity<Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){


        Client client = clientService.getClientByEmail(authentication.getName());
        Loan loan = loanService.getLoanById(loanApplicationDTO.getId());
        Account accountDestination = accountService.findAccountByNumber(loanApplicationDTO.getAccountDestination());
        double loanInteres = loanApplicationDTO.getAmount() * (loan.getInterest() / 100)  + loanApplicationDTO.getAmount();


        if(client == null){
            return new ResponseEntity<>("Client does not exist", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0 ){
            return new ResponseEntity<>("Invalid amount or payments", HttpStatus.FORBIDDEN);
        }
        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }

        if(loan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("You cannot exceed the maximum amount", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The number of payments chosen is not within those available for this loan", HttpStatus.FORBIDDEN);
        }

        if(accountDestination == null){
            return new ResponseEntity<>("Destination account cannot be empty", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(accountDestination)){
            return new ResponseEntity<>("Destinity account doesn´t exist", HttpStatus.FORBIDDEN);
        }

        if(client.getClientLoans().stream().filter(loan1 -> loan1.getLoan().getName().toString().equals(loan.getName().toString())).count() >= 1){
            return new ResponseEntity<>("the loan is already taken", HttpStatus.FORBIDDEN);
        }

        Double balanceCredit = accountDestination.getBalance() + loanApplicationDTO.getAmount();
        ClientLoan clientLoan = new ClientLoan(loan, loanInteres, loanApplicationDTO.getPayments(), client);
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), clientLoan.getLoan().getName() + " loan approved", LocalDateTime.now(), accountDestination, balanceCredit,true);
        accountDestination.setBalance(accountDestination.getBalance() + loanApplicationDTO.getAmount());
        transactionService.saveTransaction(transaction);
        clientLoanService.saveClientLoan(clientLoan);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PostMapping("/loans/admin")
    public ResponseEntity<Object> createLoan (@RequestParam String name , @RequestParam Double maxAmount
            , @RequestParam List <Integer> payments, Double interest, Authentication authentication){
        Client admin = clientService.getClientByEmail(authentication.getName());
        Loan loanName = loanService.getLoanByName(name.toLowerCase());


        if(admin == null){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(name.isEmpty()){
            return new ResponseEntity<>("Missing name", HttpStatus.FORBIDDEN);
        }

        if(payments.isEmpty()){
            return new ResponseEntity<>("Missing payments", HttpStatus.FORBIDDEN);
        }

        if(maxAmount == null){
            return new ResponseEntity<>("Missing Max Amount", HttpStatus.FORBIDDEN);
        }

        if(!(loanName == null)){
            return new ResponseEntity<>("Existing loan", HttpStatus.FORBIDDEN);
        }

        if (interest <= 0) {
            return new ResponseEntity<>("The interest must be greater than zero", HttpStatus.FORBIDDEN);
        }

        loanService.saveLoan(new Loan(name.toLowerCase(), maxAmount, payments, interest));
        return new ResponseEntity<>("Loan created successfully",HttpStatus.CREATED);
    }
}
