package com.example.tradingCards.DTO;

import com.example.tradingCards.model.Card;
import com.example.tradingCards.model.Market;
import com.example.tradingCards.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarketDTO {

    private long id;
    private Long ownerId;
    private CardDTO card;
    private int price;


}
