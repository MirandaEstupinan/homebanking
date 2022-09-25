package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();

    Account findAccountByNumber(String number);

    Account getAccountById(Long id);

    void saveAccount(Account account);

    Boolean deleteAccount(Account account);
}
