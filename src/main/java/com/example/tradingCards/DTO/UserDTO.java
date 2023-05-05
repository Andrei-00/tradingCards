package com.example.tradingCards.DTO;

import com.example.tradingCards.model.Market;
import com.example.tradingCards.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserDTO {

    private Long id;
    private String username;
    private String name;
    private String password;
    private int balance;
    // Enum from User might need to be included
    private User.Type role;
    private List<CardDTO> ownedCards = new ArrayList<>();
    private List<MarketDTO> listing;

}
