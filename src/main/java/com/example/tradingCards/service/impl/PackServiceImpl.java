package com.example.tradingCards.service.impl;

import com.example.tradingCards.DTO.PackDTO;
import com.example.tradingCards.mapper.PackMapper;
import com.example.tradingCards.model.Card;
import com.example.tradingCards.model.Pack;
import com.example.tradingCards.repository.PackRepository;
import com.example.tradingCards.service.PackService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

@Service
public class PackServiceImpl implements PackService {

    @Autowired
    private PackRepository packRepository;
    private PackMapper packMapper;

    public PackServiceImpl (PackRepository packRepository, PackMapper packMapper) {
        this.packRepository = packRepository;
        this.packMapper = packMapper;
    }


    @Override
    public void createPack(String name, String description, int price, int size) {

        Pack newPack = new Pack();
        newPack.setName(name);
        newPack.setDescription(description);
        newPack.setPrice(price);
        packRepository.save(newPack);
    }

    @Override
    public void deletePack(String name) {
        packRepository.delete(packRepository.findFirstByName(name));
    }

    @Override
    public List<PackDTO> findAll() {
        System.out.println(packRepository.findAll());
        return packRepository.findAll().
                stream().
                map(packMapper::mapModelToDTO).
                collect(Collectors.toList());
    }

    @Override
    @Transactional
    // Clean up code
    public List<Card> selectCards(Long Id) {
        Pack pack = packRepository.findById(Id)
                .orElseThrow(()
                        ->
                        new IllegalArgumentException("Invalid pack id"));


        List<Card> cardList = pack.getCardList();
        List<Double> normalizedProb = new ArrayList<>(); // Normalized probabilites that add up to 1
        //System.out.println("Card list: " + cardList);
        double sum = 0.0;
        for (Card c : cardList) {
            sum += c.getChance();
        }
        // Normalize the probabilities
        double sum1 = 0.0;
        for(Card c : cardList){
            normalizedProb.add((c.getChance() / sum));
            sum1 += c.getChance()/sum;
        }

        // Create a sum of probabilities from 0 to 1
        List<Double> sumProb = new ArrayList<>();
        sum = 0.0;
        for (Double p : normalizedProb){
            sum += p;
            sumProb.add(sum);
        }

        //System.out.println("sumProb: " + sumProb);

        // Select cards based on the new probabilities
        int packSize = pack.getSize();
        List<Card> selectedCards = new ArrayList<>();
        boolean[] selected = new boolean[cardList.size()];
        Arrays.fill(selected, false);
        for (int i = 0; i < packSize; i++) {
            //System.out.println("Rand: " + rand);
            //Optional<Double> selectedProb = sumProb.stream().dropWhile(p -> (p < rand)).findFirst();
            int index;
            do {
                double rand = Math.random();
                index = (int) sumProb.stream().takeWhile(p -> (p < rand)).count();
                if(selected[index] == false) {
                    selectedCards.add(cardList.get(index));
                }
                //System.out.println("Index: " + index.getAsInt() + " Selected: " + selectedProb.get());
                //System.out.println("Index: " + index + " Selected: " + cardList.get(index));
                //System.out.println(selected[index]);
            } while (selected[index] == true);
            selected[index] = true;
        }
        System.out.println(selectedCards);

        return selectedCards;
    }
}
