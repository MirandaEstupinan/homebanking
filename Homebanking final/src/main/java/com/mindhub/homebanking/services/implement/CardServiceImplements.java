package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImplements implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public List<Card> getAllCards(){
        return cardRepository.findAll();
    }

    @Override
    public void saveCard(Card card){
        cardRepository.save(card);
    }

    @Override
    public Boolean deleteCard(Card card){
        card.setActive(false);
        cardRepository.save(card);
        return null;
    }
    @Override
    public Card getCardByNumber(String number){
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card getCardById(Long id){
        return cardRepository.findById(id).get();
    }
}
