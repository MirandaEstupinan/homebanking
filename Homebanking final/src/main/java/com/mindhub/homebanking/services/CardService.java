package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

import java.util.List;

public interface CardService {

    List<Card> getAllCards();

    void saveCard(Card card);

    Card getCardById(Long id);
    Boolean deleteCard(Card card);

    Card getCardByNumber(String number);
}
