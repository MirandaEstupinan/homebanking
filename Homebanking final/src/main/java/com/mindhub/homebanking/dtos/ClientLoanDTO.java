package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {


    private long id;
    private String name;
    private double amount;
    private int payment;

    private long loanId;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan){
        this.name = clientLoan.getLoan().getName();
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.amount = clientLoan.getAmount();
        this.payment = clientLoan.getPayments();

    };


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayment() {
        return payment;
    }


}
