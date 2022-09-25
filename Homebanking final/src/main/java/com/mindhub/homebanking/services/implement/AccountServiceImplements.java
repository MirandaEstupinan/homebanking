package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImplements implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Account findAccountByNumber(String number){
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account getAccountById(Long id){
        return accountRepository.findById(id).get();
    }

    @Override
    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    @Override
    public Boolean deleteAccount(Account account){
        account.setAccountActive(false);
        accountRepository.save(account);
        return null;
    }

}
