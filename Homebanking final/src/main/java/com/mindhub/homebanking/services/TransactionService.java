package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    void saveTransaction(Transaction transaction);

    Set<Transaction> filterTransactionsWithDate(LocalDateTime fromDate, LocalDateTime toDate, Account account);

    Boolean deleteTransaction(Transaction transaction);
}
