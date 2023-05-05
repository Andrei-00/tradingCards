package com.example.tradingCards.service;

import com.example.tradingCards.DTO.CardDTO;
import com.example.tradingCards.DTO.PackDTO;
import com.example.tradingCards.model.Card;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PackService {

    void createPack(String name, String description, int price, int size);
    void deletePack(String name);
    List<PackDTO> findAll();

    List<Card> selectCards(Long Id);
}
