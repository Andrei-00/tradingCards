package com.example.tradingCards.service.impl;

import com.example.tradingCards.DTO.MarketDTO;
import com.example.tradingCards.mapper.MarketMapper;
import com.example.tradingCards.repository.MarketRepository;
import com.example.tradingCards.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    private MarketRepository marketRepository;
    private MarketMapper marketMapper;

    public MarketServiceImpl(MarketRepository marketRepository, MarketMapper marketMapper) {
        this.marketRepository = marketRepository;
        this.marketMapper = marketMapper;
    }

    @Override
    public List<MarketDTO> findAll() {
        return marketRepository.findAll().
                stream()
                .map(marketMapper::mapMarketToDTO)
                .collect(Collectors.toList());
    }
}
