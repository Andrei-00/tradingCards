package com.example.tradingCards.service;

import com.example.tradingCards.DTO.MarketDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MarketService {
    List<MarketDTO> findAll();


}
