package com.example.tradingCards.service;

import com.example.tradingCards.DTO.UserDTO;
import com.example.tradingCards.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {


    UserDTO findById(Long Id);
    List<UserDTO> findAll();
    UserDTO login(String username, String password);

    UserDTO modifyBalance(Long Id, int amount);

    UserDTO buyPack(Long id_user, Long id_pack);
    void createUser(Long Id, String username,String name, String password, String email, User.Type role);
    void deleteUser(User.Type role, String name);
    void deleteUserById(Long Id);

    void createListing(Long user_id, Long card_id, int price);

    void buyListing(Long user_id, Long listing_id);

}
