package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import java.time.LocalDate;

public class CardDTO {

    private long id;
    private String cardholder;
    private String number;
    private int cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private CardColor cardColor;
    private CardType cardType;

    private Boolean active;

    public CardDTO(){}

    public CardDTO(Card card){
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.cardColor = card.getCardColor();
        this.cardType = card.getCardType();
        this.active = card.getActive();
    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Boolean getActive() {
        return active;
    }
}
