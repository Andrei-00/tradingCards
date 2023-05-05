package com.example.tradingCards.controller;

import com.example.tradingCards.DTO.UserDTO;
import com.example.tradingCards.model.User;
import com.example.tradingCards.repository.UserRepository;
import com.example.tradingCards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService,
                          UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;

    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public void delete(@RequestParam Long Id){
        userService.deleteUserById(Id);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public UserDTO findById(@RequestParam Long Id){ return userService.findById(Id); }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<UserDTO> findAll(){
        return  userService.findAll();
    }

    @RequestMapping(value = "/login")
    public ResponseEntity login(@RequestBody User user){
        System.out.println(user);
        Optional<User> userFound = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        UserDTO user1;
        user1 = userService.login(user.getUsername(), user.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(user1);
    }

    @RequestMapping(value = "/modifyBalance", method = {RequestMethod.GET, RequestMethod.PUT})
    public void modifyBalance(@RequestParam Long Id, @RequestParam int sum) {userService.modifyBalance(Id, sum);}
    @RequestMapping(value = "/buyPack",  method = {RequestMethod.GET, RequestMethod.PUT})
    public void buyPack(@RequestParam Long user_id, @RequestParam Long pack_id) {userService.buyPack(user_id, pack_id);}
    @RequestMapping(value =  "/createListing",  method = {RequestMethod.GET, RequestMethod.PUT})
    public void createListing(@RequestParam Long user_id, @RequestParam Long card_id,
                              @RequestParam int price){
        userService.createListing(user_id, card_id, price);
    }

    @RequestMapping(value = "/buyListing",  method = {RequestMethod.GET, RequestMethod.PUT})
    public void buyListing(@RequestParam Long user_id, @RequestParam Long listing_id) {userService.buyListing(user_id, listing_id);}



}
