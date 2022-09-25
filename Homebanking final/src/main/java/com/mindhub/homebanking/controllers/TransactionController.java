package com.mindhub.homebanking.controllers;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.CardPaymentsDTO;
import com.mindhub.homebanking.dtos.PdfDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> newTransaction(
            @RequestParam Double amount, @RequestParam String description, String accountOrigin, String accountDestiny,
            Authentication authentication){

        Client client = clientService.getClientByEmail(authentication.getName());
        Account newAccountOrigin = accountService.findAccountByNumber(accountOrigin);
        Account newAccountDestiny = accountService.findAccountByNumber(accountDestiny);

        if(client == null){
            return new ResponseEntity<>("Client does not exist", HttpStatus.FORBIDDEN);
        }

        if(amount <= 0 ||  amount == null){
            return new ResponseEntity<>("Amount not available", HttpStatus.FORBIDDEN);
        }

        if(description.isEmpty() || accountDestiny.isEmpty() || accountOrigin.isEmpty()){
            return new ResponseEntity<>("Missing data to complete", HttpStatus.FORBIDDEN);
        }

       if(accountOrigin.equals(accountDestiny)){
           return new ResponseEntity<>("Accounts cannot be the same", HttpStatus.FORBIDDEN);
       }

       if(accountService.findAccountByNumber(accountOrigin) == null){
           return new ResponseEntity<>("Origin account doesn´t exist", HttpStatus.FORBIDDEN);
       }

       if(accountService.findAccountByNumber(accountDestiny).getAccountActive() == false){
           return new ResponseEntity<>("Destinity account is deleted", HttpStatus.FORBIDDEN);
       }

       if(accountService.findAccountByNumber(accountDestiny) == null){
           return new ResponseEntity<>("Destinity account doesn´t exist", HttpStatus.FORBIDDEN);
       }

       if(accountService.findAccountByNumber(accountOrigin).getBalance() < amount){
           return new ResponseEntity<>("Not enough money", HttpStatus.FORBIDDEN);
       }


        Double balanceDebit = newAccountOrigin.getBalance() - amount;
        Double balanceCredit = newAccountDestiny.getBalance() + amount;
       Transaction debitTransaction = new Transaction(DEBIT, -amount, description + " " + "go to" + " " + accountDestiny, LocalDateTime.now(), newAccountOrigin, balanceDebit, true);
       Transaction creditTransaction = new Transaction(CREDIT, amount, description + " " + "from" + " " +  accountOrigin, LocalDateTime.now(), newAccountDestiny, balanceCredit, true);

        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        

       newAccountOrigin.setBalance(newAccountOrigin.getBalance() - amount);
       newAccountDestiny.setBalance(newAccountDestiny.getBalance() + amount);

        accountService.saveAccount(newAccountOrigin);
        accountService.saveAccount(newAccountDestiny);
       return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/clients/current/transactions/payments")
    public ResponseEntity<Object> newPayment(@RequestBody CardPaymentsDTO cardPaymentsDTO, Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        Card cardNumber = cardService.getCardByNumber(cardPaymentsDTO.getNumber());
        Account acountOrigin = cardNumber.getAccount();
        int cvv = cardNumber.getCvv();

        if(client == null){
            return new ResponseEntity<>("client does not exist", HttpStatus.FORBIDDEN);
        }
        if(cardPaymentsDTO.getCvv() != cvv){
            return new ResponseEntity<>("invalid cvv", HttpStatus.FORBIDDEN);
        }
        if(!client.getCards().contains(cardNumber)){
            return new ResponseEntity<>("card number does not exist", HttpStatus.FORBIDDEN);
        }
        if (cardPaymentsDTO.getAmount() <= 0 ){
            return new ResponseEntity<>("invalid amount", HttpStatus.FORBIDDEN);
        }
        if(acountOrigin == null){
            return new ResponseEntity<>("account does not exist", HttpStatus.FORBIDDEN);
        }
        if(cardPaymentsDTO.getAmount() > acountOrigin.getBalance()){
            return new ResponseEntity<>("the account balance is invalid", HttpStatus.FORBIDDEN);
        }
        if(cardNumber.getActive() == false){
            return new ResponseEntity<>("disabled card", HttpStatus.FORBIDDEN);
        }

        Double balanceDebit = acountOrigin.getBalance() -cardPaymentsDTO.getAmount();
        Transaction cardTransaction = new Transaction(DEBIT, -cardPaymentsDTO.getAmount(), cardPaymentsDTO.getDescription(), LocalDateTime.now(), acountOrigin, balanceDebit, true);
        acountOrigin.setBalance(acountOrigin.getBalance() - cardPaymentsDTO.getAmount());
        transactionService.saveTransaction(cardTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transactions/filtered")
    public ResponseEntity<Object> getFilteredTransaction(
            @RequestBody PdfDTO pdfDTO, Authentication authentication) throws DocumentException, FileNotFoundException {
        Client client = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.findAccountByNumber(pdfDTO.getClientAccount());
        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("You cannot request data from an account that isn't yours.", HttpStatus.FORBIDDEN);
        }
        if (account.getTransactions().isEmpty()){
            return new ResponseEntity<>("You don't have any transactions in this account.", HttpStatus.FORBIDDEN);
        }

        if (pdfDTO.getToDate() == null){
            return new ResponseEntity<>("The date cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (pdfDTO.getFromDate() == null){
            return new ResponseEntity<>("The date cannot be empty2", HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = transactionService.filterTransactionsWithDate(pdfDTO.getFromDate(),pdfDTO.getToDate(), account);
        createPdf( transactions);
        return new ResponseEntity<>("transactions",HttpStatus.CREATED);
    }

    public static void createPdf(Set<Transaction> transactions) throws DocumentException, FileNotFoundException {
        var doc = new Document();
        String route = System.getProperty("user.home");
        PdfWriter.getInstance(doc, new FileOutputStream(route + "/Downloads/TransactionInfo.pdf"));
        doc.open();

        var bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        var paragraph = new Paragraph("Mindhub Brothers Bank");
        var table = new PdfPTable(4);
        Stream.of("Amount", "Description","Date","Type").forEach(table::addCell);

        transactions.forEach(transaction -> {
            table.addCell("$" +transaction.getAmount());
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getDate().toString());
            table.addCell(transaction.getType().toString());
        });

        paragraph.add(table);
        doc.add(paragraph);
        doc.close();
    }


    }
