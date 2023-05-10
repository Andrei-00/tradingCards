package com.example.tradingCards.model;


import jakarta.persistence.*;
import lombok.*;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String color;
    private String type;
    private String position;


    /*
        private Integer pace;
        private Integer dribbling;
        private Integer shoot;
        private Integer defending;
        private Integer passing;
        private Integer physical;*/
    private  int overall;
    private int minPrice;
    private int maxPrice;
    private Double chance;

    @ManyToMany(mappedBy = "ownedCards")
    private List<User> owners;

    @ManyToMany (mappedBy = "cardList")
    private List<Pack> packList = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.MERGE)
    private Player player;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Market> listing;

    public void addToPackList(Pack pack){
        packList.add(pack);
    }

    public void addToOwners(User user){
        owners.add(user);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", position='" + position + '\'' +
                ", overall=" + overall +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", chance=" + chance +
                //", owners=" + owners +
                //", packList=" + packList +
                ", player=" + player +
                ", listing=" + listing +
                '}';
    }
}