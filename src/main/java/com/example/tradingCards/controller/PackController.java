package com.example.tradingCards.controller;

import com.example.tradingCards.DTO.PackDTO;
import com.example.tradingCards.service.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pack")

public class PackController {

    @Autowired
    private PackService packService;

    public PackController(PackService packService){
        this.packService = packService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PackDTO> findAll() { return packService.findAll(); }



}
