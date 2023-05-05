package com.example.tradingCards.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity



public class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private int balance;


    public enum Type {
        REGULAR,
        ADMIN,
    }

    private Type role;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "card_owner",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_card"))
    List<Card> ownedCards = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Market> listings;

    public void addToOwnedCards(Card card) {
        ownedCards.add(card);
    }

    @Override
    public int compareTo(User o) {
        return Long.compare(this.getId(), o.getId());
    }

}
