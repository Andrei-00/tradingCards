package com.example.tradingCards.mapper;

import com.example.tradingCards.DTO.CardDTO;
import com.example.tradingCards.DTO.UserDTO;
import com.example.tradingCards.model.Card;
import com.example.tradingCards.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    CardMapper cardMapper = new CardMapper();
    //MarketMapper marketMapper = new MarketMapper();

    public UserDTO mapModelToDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setBalance(user.getBalance());
        userDTO.setOwnedCards(user.getOwnedCards().
                stream()
                .map(cardMapper::mapModelToDto)
                .collect(Collectors.toList()));
        /*userDTO.setListing(user.getListings().
                stream()
                .map(marketMapper::mapMarketToDTOUser)
                .collect(Collectors.toList()));
        */

        System.out.println("User ownedCards: " + user.getOwnedCards());
        System.out.println("DTO ownedCards: " + userDTO.getOwnedCards());

        return userDTO;
    }

    public UserDTO mapModelToDtoMarket(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setBalance(user.getBalance());
        userDTO.setOwnedCards(user.getOwnedCards().
                stream()
                .map(cardMapper::mapModelToDto)
                .collect(Collectors.toList()));


        System.out.println("User ownedCards: " + user.getOwnedCards());
        System.out.println("DTO ownedCards: " + userDTO.getOwnedCards());

        return userDTO;
    }

}
