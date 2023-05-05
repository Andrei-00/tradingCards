package com.example.tradingCards.mapper;

import com.example.tradingCards.DTO.PackDTO;
import com.example.tradingCards.model.Pack;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PackMapper {

    CardMapper cardMapper = new CardMapper();
    public PackDTO mapModelToDTO(Pack pack){
        PackDTO packDTO = new PackDTO();
        packDTO.setId(pack.getId());
        packDTO.setName(pack.getName());
        packDTO.setDescription(pack.getDescription());
        packDTO.setPrice(pack.getPrice());
        packDTO.setSize(pack.getSize());
        packDTO.setCardList(pack.getCardList().
                stream()
                .map(cardMapper::mapModelToDto)
                .collect(Collectors.toList()));

        return packDTO;
    }
}
