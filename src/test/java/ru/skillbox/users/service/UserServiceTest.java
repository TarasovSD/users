package ru.skillbox.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.skillbox.users.dto.UserDto;
import ru.skillbox.users.dto.UserFullDto;
import ru.skillbox.users.entity.City;
import ru.skillbox.users.entity.Gender;
import ru.skillbox.users.entity.Subscription;
import ru.skillbox.users.entity.User;
import ru.skillbox.users.exception.CityNotFoundException;
import ru.skillbox.users.exception.SubscriptionNotFoundException;
import ru.skillbox.users.exception.UserNotFoundException;
import ru.skillbox.users.mapper.UserMapper;
import ru.skillbox.users.repository.CityRepository;
import ru.skillbox.users.repository.SubscriptionRepository;
import ru.skillbox.users.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    UserRepository userRepository;
    CityRepository cityRepository;
    SubscriptionRepository subscriptionRepository;
    UserService userService;
    City city;
    UserDto userDtoForSave;
    User savedUser1;
    User user2;
    User user3;


    @BeforeEach
    void init() {
        userRepository = Mockito.mock(UserRepository.class);
        cityRepository = Mockito.mock(CityRepository.class);
        subscriptionRepository = Mockito.mock(SubscriptionRepository.class);
        userService = new UserService(userRepository, cityRepository, subscriptionRepository);
        city = new City(1, "Moscow");
        userDtoForSave = new UserDto(null,
                "User1First",
                "User1Last",
                "User1Patr",
                Gender.MALE,
                "11.11.2001",
                1,
                "link1",
                "description1",
                "nickname",
                "email1@ya.ru",
                "79215674567"
        );
        savedUser1 = new User(1, "User1First",
                "User1Last",
                "User1Patr",
                Gender.MALE,
                "11.11.2001",
                city,
                "link1",
                "description1",
                "nickname",
                "email1@ya.ru",
                "79215674567");
        user2 = new User(2, "User2First",
                "User2Last",
                "User2Patr",
                Gender.MALE,
                "11.10.1999",
                city,
                "link2",
                "description2",
                "nickname2",
                "email2@ya.ru",
                "79215674562");
        user3 = new User(3, "User3First",
                "User3Last",
                "User3Patr",
                Gender.MALE,
                "11.10.1998",
                city,
                "link3",
                "description3",
                "nickname3",
                "email3@ya.ru",
                "79215674563");
    }

    @Test
    public void createUser() {
        User userForSave = UserMapper.toUser(userDtoForSave, city);

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        Mockito.when(userRepository.save(userForSave)).thenReturn(savedUser1);

        UserFullDto userFullDtoForCheck = userService.createUser(userDtoForSave);

        assertEquals(1, userFullDtoForCheck.id());
        assertEquals("User1First", userFullDtoForCheck.firstName());
        assertEquals("User1Last", userFullDtoForCheck.lastName());
        assertEquals("User1Patr", userFullDtoForCheck.patronymic());
        assertEquals(Gender.MALE, userFullDtoForCheck.gender());
        assertEquals("11.11.2001", userFullDtoForCheck.birthday());
        assertEquals(city.getName(), userFullDtoForCheck.city().name());
        assertEquals("link1", userFullDtoForCheck.link());
        assertEquals("description1", userFullDtoForCheck.description());
        assertEquals("nickname", userFullDtoForCheck.nickname());
        assertEquals("email1@ya.ru", userFullDtoForCheck.email());
        assertEquals("79215674567", userFullDtoForCheck.phone());

        verify(cityRepository, times(1))
                .findById(anyInt());

        verify(userRepository, times(1))
                .save(any());

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> userService.createUser(userDtoForSave));
    }

    @Test
    public void getUser() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(savedUser1));

        UserFullDto userFullDtoForCheck = userService.getUser(1);

        assertEquals(1, userFullDtoForCheck.id());
        assertEquals("User1First", userFullDtoForCheck.firstName());
        assertEquals("User1Last", userFullDtoForCheck.lastName());
        assertEquals("User1Patr", userFullDtoForCheck.patronymic());
        assertEquals(Gender.MALE, userFullDtoForCheck.gender());
        assertEquals("11.11.2001", userFullDtoForCheck.birthday());
        assertEquals(city.getName(), userFullDtoForCheck.city().name());
        assertEquals("link1", userFullDtoForCheck.link());
        assertEquals("description1", userFullDtoForCheck.description());
        assertEquals("nickname", userFullDtoForCheck.nickname());
        assertEquals("email1@ya.ru", userFullDtoForCheck.email());
        assertEquals("79215674567", userFullDtoForCheck.phone());

        verify(userRepository, times(1))
                .findById(anyInt());

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(anyInt()));
    }

    @Test
    public void deleteUser() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(savedUser1));
        userService.deleteUser(savedUser1.getId());

        verify(userRepository, times(1))
                .findById(anyInt());

        verify(userRepository, times(1))
                .delete(any());

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(savedUser1.getId()));
    }

    @Test
    public void updateUser() {
        UserDto userDtoForUpdate = new UserDto(1,
                "UpdatedFirst",
                "UpdatedLast",
                "UpdatedPatr",
                Gender.MALE,
                "10.10.2007",
                1,
                "Updated link1",
                "Updated description1",
                "Updated nickname",
                "updated@ya.ru",
                "79215674579"
        );

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDtoForUpdate, 2));

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDtoForUpdate, 1));

        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(savedUser1));
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        Mockito.when(userRepository.save(UserMapper.toUser(userDtoForUpdate, city))).thenReturn(savedUser1);

        UserFullDto userFullDtoForCheck = userService.updateUser(userDtoForUpdate, 1);

        assertEquals(userFullDtoForCheck, UserMapper.toUserFullDto(UserMapper.toUser(userDtoForUpdate, city)));

        verify(userRepository, times(2))
                .findById(anyInt());

        verify(cityRepository, times(1))
                .findById(anyInt());

        verify(userRepository, times(1))
                .save(any());

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> userService.updateUser(userDtoForUpdate, 1));
    }

    @Test
    public void subscribeToUser() {
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user2));
        Mockito.when(userRepository.findById(3)).thenReturn(Optional.ofNullable(user3));
        Mockito.when(subscriptionRepository.save(any())).thenReturn(new Subscription(user2, user3));

        userService.subscribeToUser(2, 3);

        verify(userRepository, times(2))
                .findById(anyInt());
        verify(subscriptionRepository, times(1)).save(any());

        Mockito.when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.subscribeToUser(2, 3));

        Mockito.when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user2));
        Mockito.when(userRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.subscribeToUser(2, 3));
    }

    @Test
    public void unsubscribeToUser() {
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user2));
        Mockito.when(userRepository.findById(3)).thenReturn(Optional.ofNullable(user3));
        Mockito.when(subscriptionRepository.findBySubscriberAndRespondent(user2, user3))
                .thenReturn(Optional.of(new Subscription(user2, user3)));

        userService.unsubscribeFromUser(2, 3);

        verify(userRepository, times(2))
                .findById(anyInt());

        verify(subscriptionRepository, times(1))
                .findBySubscriberAndRespondent(user2, user3);

        Mockito.when(subscriptionRepository.findBySubscriberAndRespondent(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> userService.unsubscribeFromUser(2, 3));

        Mockito.when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.unsubscribeFromUser(2, 3));

        Mockito.when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user2));
        Mockito.when(userRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.unsubscribeFromUser(2, 3));
    }
}