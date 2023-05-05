package com.example.tradingCards.service.impl;

import com.example.tradingCards.model.Pack;
import com.example.tradingCards.repository.PackRepository;
import com.example.tradingCards.service.PackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PackServiceImplTest {

    private PackRepository packRepository;

    private PackService packService;

    @Autowired
    public PackServiceImplTest (PackRepository packRepository, PackService packService) { this.packRepository = packRepository; this.packService = packService; }

    @Test
    void createPack() {

        packService.createPack("Test", "asd", 500, 1);
        assertThat(packRepository.findFirstByName("Test")).isNotNull();

    }

    @Test
    void deletePack() {
        packService.createPack("Test2", "asd",500, 1);
        packService.deletePack("Test2");
        assertThat(packRepository.findFirstByName("Test2")).isNull();
    }
}