package com.example.tradingCards;

import com.example.tradingCards.DTO.UserDTO;
import com.example.tradingCards.model.*;
import com.example.tradingCards.repository.*;
import com.example.tradingCards.service.CardService;
import com.example.tradingCards.service.PackService;
import com.example.tradingCards.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EntityScan
@EnableJpaRepositories
@SpringBootApplication
public class TradingCardsApplication {


	public static void main(String[] args){

		SpringApplication.run(TradingCardsApplication.class, args);
	}

	void checkLogin(int loginValue){

		if (loginValue >= 0){
			System.out.println("Successful login!");
		} else {
			System.out.println("Unsuccessful login!");
		}
	}

	@Bean
	CommandLineRunner init(CardRepository cardRepository, UserRepository userRepository, MarketRepository marketRepository,
						   PackRepository packRepository, PlayerRepository playerRepository,
						   UserService userService, CardService cardService,
						   PackService packService){
		return args -> {

			//User login
			User user = new User();
			user.setUsername("user1");
			user.setName("User1");
			user.setPassword("1234");
			user.setRole(User.Type.REGULAR);
			userRepository.save(user);

			userService.login("user1", "1234");


			userService.login("User", "");


			//Admin actions
			User admin = new User();
			admin.setUsername("admin1");
			admin.setName("Admin1");
			admin.setPassword("1234");
			admin.setRole(User.Type.ADMIN);
			admin.setBalance(100);
			userRepository.save(admin);

			User user1 = new User();
			user1.setUsername("user1");
			user1.setName("User1");
			user1.setPassword("");
			user1.setRole(User.Type.REGULAR);
			user1.setBalance(100);
			userRepository.save(user1);

			UserDTO adminDTO = userService.login("admin1", "1234");
			if (adminDTO != null){
				userService.createUser(admin.getId(), "admin","User2", "aaa", "", User.Type.REGULAR);
				userService.login("admin", "aaa");


				userService.deleteUser(admin.getRole(), "User1");
			}

			//userService.deleteUserById(3L);
			userService.deleteUserById(200L);

			//Cards
			Card card = new Card();
			card.setType("Gold");
			card.setPosition("ST");
			card.setMaxPrice(200);
			card.setMinPrice(100);
			card.setChance(0.5);
			card.setOverall(90);
			cardRepository.save(card);

			Card card1 = new Card();
			card1.setType("Silver");
			card1.setPosition("GK");
			card1.setMaxPrice(5000);
			card1.setMinPrice(10);
			card1.setChance(0.7);
			card1.setOverall(74);
			cardRepository.save(card1);

			List<Card> cardList = new ArrayList<>();
			cardList.add(card1);
			cardList.add(card);
			for (int i = 2; i < 10; i++){
				Card newCard = new Card();
				String type = "Silver" + i;
				newCard.setType(type);
				newCard.setPosition("GK");
				newCard.setMaxPrice(5000);
				newCard.setMinPrice(10);
				newCard.setChance(1.0/i);
				newCard.setOverall(74);
				cardRepository.save(newCard);
				cardList.add(newCard);
			}

			//Packs
			Pack pack= new Pack();
			pack.setPrice(5);
			pack.setName("Regular pack");
			pack.setDescription("Desc");
			pack.setSize(5);

			pack.getCardList().addAll(cardList);

			packRepository.save(pack);


			Pack pack1 = new Pack();

			List<Pack> packList = new ArrayList<>();
			packList.add(pack);
			card.setPackList(packList);
			cardRepository.save(card);
			System.out.println("Card packList: " + card.getPackList());
			System.out.println("Pack cardList: " + pack.getCardList());
			admin.getOwnedCards().addAll(Arrays.asList(card, card1));
			userRepository.save(admin);
			System.out.println("Admin: " + admin);

			packService.selectCards(pack.getId());

			//System.out.println("Before buying the pack: " + admin.getOwnedCards());
			userService.buyPack(2l, 1l);
			//System.out.println("After buying the pack: " + admin.getOwnedCards());

			//userService.createListing(2l, 2l, 100);

		};
	}


}
