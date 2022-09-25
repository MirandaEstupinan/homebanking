package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplements implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    @Override
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    @Override
    public Set<Transaction> filterTransactionsWithDate(LocalDateTime fromDate, LocalDateTime toDate, Account account){
        return transactionRepository.findByDateBetween(fromDate, toDate).stream().filter(transaction -> transaction.getAccount() == account).collect(Collectors.toSet());
    }


    @Override
    public Boolean deleteTransaction(Transaction transaction){
        transaction.setActive(false);
        transactionRepository.save(transaction);
        return null;
    }
}
