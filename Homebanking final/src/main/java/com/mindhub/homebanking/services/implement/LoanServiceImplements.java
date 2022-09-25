package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImplements implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<Loan> getAllLoans(){
        return loanRepository.findAll();
    }
    @Override
    public Loan getLoanById(Long id){
        return loanRepository.findById(id).get();
    }

    @Override
    public Loan getLoanByName(String name){
        return loanRepository.findByName(name);
    }
    @Override
    public void saveLoan(Loan loan){
        loanRepository.save(loan);
    }
}
