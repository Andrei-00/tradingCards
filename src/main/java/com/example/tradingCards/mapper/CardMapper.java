package com.example.tradingCards.mapper;

import com.example.tradingCards.DTO.CardDTO;
import com.example.tradingCards.DTO.UserDTO;
import com.example.tradingCards.model.Card;
import com.example.tradingCards.model.User;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardDTO mapModelToDto(Card card){
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(card.getId());
        cardDTO.setChance(card.getChance());
        cardDTO.setMaxPrice(card.getMaxPrice());
        cardDTO.setMinPrice(card.getMinPrice());
        cardDTO.setPosition(card.getPosition());
        cardDTO.setType(card.getType());
        cardDTO.setOverall(card.getOverall());


        return cardDTO;
    }


}
