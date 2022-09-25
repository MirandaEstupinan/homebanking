package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {

    private long id;
    private Double amount;
    private int payments;
    private String accountDestination;

    public LoanApplicationDTO(){};

    public LoanApplicationDTO(Loan loan, double amount, Integer payments, String accountDestination){
        this.id = loan.getId();
        this.amount = amount;
        this.payments = payments;
        this.accountDestination = accountDestination;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountDestination() {
        return accountDestination;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public void setAccountDestination(String accountDestination) {
        this.accountDestination = accountDestination;
    }
}
