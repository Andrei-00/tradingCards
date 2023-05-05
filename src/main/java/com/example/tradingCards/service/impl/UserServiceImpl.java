package com.example.tradingCards.service.impl;

import com.example.tradingCards.DTO.UserDTO;
import com.example.tradingCards.mapper.UserMapper;
import com.example.tradingCards.model.Card;
import com.example.tradingCards.model.Market;
import com.example.tradingCards.model.Pack;
import com.example.tradingCards.model.User;
import com.example.tradingCards.repository.CardRepository;
import com.example.tradingCards.repository.MarketRepository;
import com.example.tradingCards.repository.PackRepository;
import com.example.tradingCards.repository.UserRepository;
import com.example.tradingCards.service.PackService;
import com.example.tradingCards.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PackRepository packRepository;
    private PackService packService;
    private CardRepository cardRepository;
    private MarketRepository marketRepository;

    public UserServiceImpl (UserRepository userRepository, PackRepository packRepository,
                            CardRepository cardRepository, PackService packService,
                            MarketRepository marketRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.packRepository = packRepository;
        this.packService = packService;
        this.cardRepository = cardRepository;
        this.marketRepository = marketRepository;
    }

    @Override
    public UserDTO findById(Long Id) {
        User user = userRepository.findById(Id)
                .orElseThrow(()
                        ->
                        new IllegalArgumentException("Invalid user id"));
        return  userMapper.mapModelToDto(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().
                stream().
                map(userMapper::mapModelToDto).
                collect(Collectors.toList());
    }

    @Override
    public UserDTO login(String username, String password) {
        try {
            final User user = userRepository.findByUsernameAndPassword(username, password)
                    .<EntityNotFoundException>orElseThrow(()
                            ->
                    {
                        throw new EntityNotFoundException("Username or password is incorrect");
                    });
            System.out.println("Successful login");
            return userMapper.mapModelToDto(user);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }


        return null;
    }

    @Override
    public UserDTO modifyBalance(Long Id, int amount) {
        try {
            User user = userRepository.findById(Id)
                    .orElseThrow(()
                            ->
                            new IllegalArgumentException("Invalid user id"));

            int currentBalance = user.getBalance();
            int newBalance = currentBalance + amount;
            if (newBalance < 0){
                throw new RuntimeException("Balance for user with id " + user.getUsername() + " is too small");
            }
            user.setBalance(newBalance);
            userRepository.save(user);
            System.out.println("User Dto: " + userMapper.mapModelToDto(user));
            return userMapper.mapModelToDto(user);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public UserDTO buyPack(Long id_user, Long id_pack) {
        try {
            System.out.println("Buy Pack");
            User user = userRepository.findById(id_user)
                    .orElseThrow(()
                        -> new IllegalArgumentException("Invalid user id"));

            Pack pack = packRepository.findById(id_pack)
                    .orElseThrow(()
                            -> new IllegalArgumentException("Invalid pack id"));

            int packPrice = pack.getPrice();
            // Check if user can buy the pack
            UserDTO userDTO = modifyBalance(id_user, -packPrice);
            if (userDTO == null){
                throw new RuntimeException("Balance of user: " + user.getId() + " is too low");
            }

            List<Card> selectCards = packService.selectCards(id_pack);
            System.out.println(selectCards.size());
            // Add the new aquired cards to the user's card list
            user.getOwnedCards().addAll(selectCards);
            userRepository.save(user);

            return userMapper.mapModelToDto(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public void createUser(Long Id, String newUsername, String newName, String newPassword, String newEmail, User.Type newRole) {

        User user = userRepository.findById(Id)
                .orElseThrow(()
                        -> new IllegalArgumentException("Invalid user id"));

        if(user.getRole() != User.Type.ADMIN) {
            System.out.println("Not an admin!");
            return;
        }

        User newUser = new User();
        newUser.setUsername(newUsername);
        newUser.setName(newName);
        newUser.setPassword(newPassword);
        newUser.setEmail(newEmail);
        newUser.setRole(newRole);
        userRepository.save(newUser);
    }

    @Override
    public void deleteUser(User.Type role, String name) {

        if(role != User.Type.ADMIN) {
            System.out.println("Not an admin!");
            return;
        }

        userRepository.delete(userRepository.findFirstByName(name));
    }

    @Override
    public void deleteUserById(Long Id) {
        try {
            User user = userRepository.findById(Id)
                    .orElseThrow(() ->
                    {
                        throw new IllegalArgumentException("There is no user with Id: " + Id);
                    });
            userRepository.delete(user);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void createListing(Long user_id, Long card_id, int price) {
        try {
            User user = userRepository.findById(user_id)
                    .orElseThrow(()
                            ->
                        new IllegalArgumentException("There is no user with Id: " + user_id));


            Card card = cardRepository.findById(card_id)
                    .orElseThrow(()
                            ->
                        new IllegalArgumentException("There is no card with Id: " + card_id));

            List<Card> cardList = user.getOwnedCards();
            boolean cardExists = false;
            for(Card c : cardList){
                if (c.toString().equals(card.toString()))
                    cardExists = true;
            }

            if (cardExists == false){
                throw new RuntimeException("User with Id: " + user_id + " does not own card with Id: " + card_id);
            }

            if (price > card.getMaxPrice() || price < card.getMinPrice()) {
                throw new IllegalArgumentException("The given price of: " + price + " is invalid for card with" +
                        "Id: " + card_id);
            }

            // Create the listing on the market
            Market listing = new Market();
            listing.setCard(card);
            listing.setOwner(user);
            listing.setPrice(price);
            marketRepository.save(listing);

            // Remove listed card from the owner
            user.getOwnedCards().remove(card);
            userRepository.save(user);
            }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void buyListing(Long user_id, Long listing_id) {

        try {
            User user = userRepository.findById(user_id)
                    .orElseThrow(()
                            ->
                            new IllegalArgumentException("There is no user with Id: " + user_id));

            Market listing = marketRepository.findById(listing_id)
                    .orElseThrow(()
                            ->
                            new IllegalArgumentException("There is no listing with Id: " + listing_id));


            UserDTO userDTO = modifyBalance(user.getId(), -listing.getPrice());
            if (userDTO == null){
                throw new RuntimeException("User with Id: " + user_id + " can't buy listing with id: " + listing_id);
            }

            // If user has enough money update his owned cards and delete the listing
            user.getOwnedCards().add(listing.getCard());
            userRepository.save(user);
            marketRepository.delete(listing);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}
