package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDate;

public class CardPaymentsDTO {

    private LocalDate thruDate;

    private String cardholder;
    private long id;

    private String number;
    private Integer cvv;
    private String description;
    private Double amount;

    public CardPaymentsDTO(){}

    public CardPaymentsDTO(String number, int cvv, String description, Double amount, String cardholder, LocalDate thruDate){
        this.number = number;
        this.cvv = cvv;
        this.description = description;
        this.amount = amount;
        this.cardholder = cardholder;
        this.thruDate = thruDate;

    }



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getCardholder() {
        return cardholder;
    }


}
