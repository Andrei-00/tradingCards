package com.example.tradingCards.mapper;

import com.example.tradingCards.DTO.MarketDTO;
import com.example.tradingCards.model.Card;
import com.example.tradingCards.model.Market;
import org.springframework.stereotype.Component;

@Component
public class MarketMapper {

    UserMapper userMapper = new UserMapper();
    CardMapper cardMapper = new CardMapper();

    public MarketDTO mapMarketToDTO(Market market){
        MarketDTO marketDTO = new MarketDTO();
        marketDTO.setId(market.getId());
        marketDTO.setOwnerId(market.getOwner().getId());
        marketDTO.setCard(cardMapper.mapModelToDto(market.getCard()));
        marketDTO.setPrice(market.getPrice());
        return marketDTO;
    }

    public MarketDTO mapMarketToDTOUser(Market market){
        MarketDTO marketDTO = new MarketDTO();
        marketDTO.setId(market.getId());
        marketDTO.setCard(cardMapper.mapModelToDto(market.getCard()));
        marketDTO.setPrice(market.getPrice());
        return marketDTO;
    }
}
