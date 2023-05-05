package com.example.tradingCards.controller;

import com.example.tradingCards.DTO.MarketDTO;
import com.example.tradingCards.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/market")
public class MarketController {

    @Autowired
    private MarketService marketService;
    public MarketController(MarketService marketService){
        this.marketService = marketService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<MarketDTO> findAll() {return  marketService.findAll();}

}
