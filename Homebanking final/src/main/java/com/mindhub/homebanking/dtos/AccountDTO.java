package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;
    private double balance;
    private String number;
    private LocalDateTime creationDate;
    private Set<TransactionDTO> transactions;

    private AccountType accountType;

    private Boolean accountActive;

    public AccountDTO() {
    }

    public AccountDTO (Account account){
        this.id = account.getId();
        this.balance = account.getBalance();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.accountActive = account.getAccountActive();
        this.accountType = account.getAccountType();
    }

    public long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }


    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public Boolean getAccountActive() {
        return accountActive;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
