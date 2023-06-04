package ru.skillbox.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.skillbox.users.entity.City;
import ru.skillbox.users.entity.HardSkill;
import ru.skillbox.users.entity.Subscription;
import ru.skillbox.users.entity.User;
import ru.skillbox.users.repository.CityRepository;
import ru.skillbox.users.repository.HardSkillRepository;
import ru.skillbox.users.repository.SubscriptionRepository;
import ru.skillbox.users.repository.UserRepository;

import java.util.Set;

@SpringBootApplication
public class UsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }

    @Bean
    CommandLineRunner usersJpa(CityRepository cityRepository, HardSkillRepository hardSkillRepository,
                               UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        return args -> {
            City moskva = new City("Москва");
            City spb = new City("Санкт Петербург");

            cityRepository.save(moskva);
            cityRepository.save(spb);

            cityRepository.delete(moskva);

            for (City city : cityRepository.findAll()) {
                System.out.println(city);
            }

            HardSkill one = new HardSkill("one");
            HardSkill two = new HardSkill("two");

            hardSkillRepository.save(one);
            hardSkillRepository.save(two);

            hardSkillRepository.delete(one);

            for (HardSkill hardSkill : hardSkillRepository.findAll()) {
                System.out.println(hardSkill);
            }

            User userOne = new User("One", "Ones", "Once", "man",
                    "11.12.2001", spb, "123123", "desc", "one",
                    "csdc@ccd.ru", "23123233");
            User userTwo = new User("Two", "Twos", "Twice", "man",
                    "11.12.2002", spb, "123wqw123", "desc", "two",
                    "cssdcdc@ccd.ru", "34123233");

            userRepository.save(userOne);
            userRepository.save(userTwo);

            for (User user: userRepository.findAll()) {
                System.out.println(user);
            }

            Set<HardSkill> hardSkills = userOne.getHardSkills();

            userOne.addHardSkill(two);

            System.out.println("Скилы userOne:");
            for (HardSkill h: hardSkills) {
                System.out.println(h);
            }

            Subscription subscription = new Subscription(userOne, userTwo);
            subscriptionRepository.save(subscription);
            System.out.println("Подписки userOne:");
            for (Subscription s : subscriptionRepository.findAllBySubscriber(userOne)) {
                System.out.println(s);
            }
        };
    }
}
