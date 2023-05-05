package com.example.tradingCards.service.impl;

import com.example.tradingCards.DTO.UserDTO;
import com.example.tradingCards.mapper.CardMapper;
import com.example.tradingCards.mapper.UserMapper;
import com.example.tradingCards.model.Card;
import com.example.tradingCards.model.Market;
import com.example.tradingCards.model.Pack;
import com.example.tradingCards.model.User;
import com.example.tradingCards.repository.CardRepository;
import com.example.tradingCards.repository.MarketRepository;
import com.example.tradingCards.repository.PackRepository;
import com.example.tradingCards.repository.UserRepository;
import com.example.tradingCards.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.error.Mark;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserServiceImplTest {

    private UserRepository userRepository;
    private UserService userService;
    private UserMapper userMapper;
    private CardRepository cardRepository;
    private CardMapper cardMapper;
    private PackRepository packRepository;
    private MarketRepository marketRepository;
    private Faker faker;
    private List<String> names;
    private final String nonExistent = "Dummy name";
    Comparator<UserDTO> byId = Comparator.comparingLong(UserDTO::getId);

    @Autowired
    public UserServiceImplTest (UserRepository userRepository, UserService userService,
                                UserMapper userMapper, CardRepository cardRepository,
                                CardMapper cardMapper, PackRepository packRepository,
                                MarketRepository marketRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.packRepository = packRepository;
        this.marketRepository = marketRepository;
        faker = new Faker();
        names = new ArrayList<>();
    }

    User createRegUser(){
        User user = new User();
        names.add(faker.name().fullName());
        user.setName(names.get(0));
        user.setPassword("1234");
        user.setRole(User.Type.REGULAR);
        userRepository.save(user);

        return user;
    }

    User createAdminUser(){
        User user = new User();
        names.add(faker.name().fullName());
        user.setName(names.get(0));
        user.setPassword("1234");
        user.setRole(User.Type.ADMIN);
        userRepository.save(user);

        return user;
    }



    @Test
    void login() {

        User user = new User();
        names.add(faker.name().fullName());
        user.setName(names.get(names.size()-1));
        user.setPassword("1234");
        user.setRole(User.Type.REGULAR);
        userRepository.save(user);
        assertThat(userRepository.findFirstByName(names.get(names.size()-1)));
        assertThat(userRepository.findFirstByName(nonExistent));

    }

    @Test
    void createUser() {

        User user = new User();
        //System.out.println(names.size());
        user.setName("Test Admin");
        user.setPassword("1234");
        user.setRole(User.Type.ADMIN);
        userRepository.save(user);

        names.add(faker.name().fullName());
        userService.createUser(user.getId(), "John2", names.get(0),"", "", User.Type.REGULAR);
        assertThat(userRepository.findFirstByName(names.get(0))).isNotNull();


        names.add(faker.name().fullName());
        userService.createUser(user.getId(), "John4", names.get(1), "", "", User.Type.REGULAR);
        assertThat(userRepository.findFirstByName(names.get(1))).isNotNull();
        assertThat(userRepository.findFirstByName(nonExistent)).isNull();
    }

    @Test
    void deleteUser() {

        User user = new User();
        user.setName("John One");
        user.setPassword("1234");
        user.setRole(User.Type.ADMIN);

        User user2 = new User();
        names.add(faker.name().fullName());
        user2.setName(names.get(0));
        user2.setPassword("1234");
        user2.setRole(User.Type.REGULAR);
        userRepository.save(user2);

        userService.deleteUser(user.getRole(), names.get(0));

        assertThat(userRepository.findFirstByName("John Two")).isNull();

        User user1 = new User();
        names.add(faker.name().fullName());
        user1.setName(names.get(1));
        user1.setPassword("1234");
        user1.setRole(User.Type.REGULAR);
        userRepository.save(user1);
        userRepository.save(user2);

        userService.deleteUser(user2.getRole(),  names.get(1));

        assertThat(userRepository.findFirstByName(names.get(1))).isNotNull();

    }

    @Test
    void findById() {
        User user = createRegUser();
        assertThat(userRepository.findById(user.getId())).isNotNull();
    }

    @Test
    void findAll() {
        List<User> users = new ArrayList<>();
        users.add(createRegUser());
        users.add(createRegUser());
        users.add(createAdminUser());
        userRepository.saveAll(users);

        List<UserDTO> userDTOS = userService.findAll();

        Collections.sort(users);

        Collections.sort(userDTOS, byId);

        System.out.println(users);
        System.out.println(userDTOS);

        for (int i = 0; i < users.size(); i++){

            UserDTO userDTO = userMapper.mapModelToDto(users.get(i));
            if(users.contains(userDTO)) {
                System.out.println(userDTOS.get(i));
                System.out.println(userDTO);
                assertThat(userDTO.equals(userDTOS.get(i))).isTrue();
            }
        }

    }

    @Test
    void modifyBalance() {
        User user = createRegUser();
        user.setBalance(10);
        userRepository.save(user);
        userService.modifyBalance(user.getId(), -5);
        Optional<User> user1;
        user1 = userRepository.findById(user.getId());
        assertThat(user1.get().getBalance()).isEqualTo(5);

    }

    @Test
    void buyPack() {
        User user = createRegUser();
        List<Card> cardList = new ArrayList<>();
        Card card = new Card();
        card.setType("Gold");
        card.setOverall(90);
        card.setChance(1.0);
        cardRepository.save(card);
        cardList.add(card);

        Card card1 = new Card();
        card1.setType("Icon");
        card1.setOverall(95);
        card1.setChance(1.0);
        cardRepository.save(card1);
        cardList.add(card1);


        Pack pack = new Pack();
        pack.setSize(2);
        pack.getCardList().addAll(cardList);
        packRepository.save(pack);

        UserDTO userDTO = userService.buyPack(user.getId(), pack.getId());

        for (int i = 0; i < pack.getSize(); i++){
            assertThat(cardMapper.mapModelToDto(cardList.get(i))).isEqualTo(userDTO.getOwnedCards().get(i));
        }



    }

    @Test
    void deleteUserById() {
        User user = createRegUser();
        userService.deleteUserById(user.getId());
        Optional<User> empty = Optional.empty();
        assertThat(userRepository.findById(user.getId())).isEqualTo(empty);
    }

    @Test
    void createListing() {
        User user = createRegUser();
        List<Card> cardList = new ArrayList<>();
        Card card = new Card();
        card.setType("Gold");
        card.setOverall(90);
        card.setChance(1.0);
        cardRepository.save(card);
        cardList.add(card);
        userService.createListing(user.getId(), card.getId(), 5);
        //Market market = marketRepository.findById();
    }

    @Test
    void buyListing() {
    }
}