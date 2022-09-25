package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Loan;
import java.util.List;

public class LoanDTO {

    private long id;
    private String name;
    private Double maxAmount;
    private List<Integer> payments;

    private Double interest;

    public LoanDTO(){
    };

    public LoanDTO(Loan loan){
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();;
        this.payments = loan.getPayments();
        this.id = loan.getId();
        this.interest = loan.getInterest();
    };

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public long getId() {
        return id;
    }

    public Double getInterest() {
        return interest;
    }
}
