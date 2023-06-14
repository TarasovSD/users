package ru.skillbox.users.service;

import org.springframework.stereotype.Service;
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

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CityRepository cityRepository;

    private final SubscriptionRepository subscriptionRepository;

    public UserService(UserRepository userRepository, CityRepository cityRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public String createUser(UserDto userDto) {
        Integer cityId = userDto.cityId();
        City city = cityRepository.findById(cityId).orElseThrow(() ->
                new CityNotFoundException(String.format("Город с id = %s не найден", cityId)));
        User user = UserMapper.toUser(userDto, city);
        User createdUser = userRepository.save(user);
        return String.format("Пользователь %s c id = %s сохранен в базу данных",
                createdUser.getLastName(),
                createdUser.getId());
    }

    public UserFullDto getUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId)));
        return UserMapper.toUserFullDto(user);
    }

    public void deleteUser(Integer userId) {
        User userToDelete = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId)));
        userRepository.delete(userToDelete);
    }

    public String updateUser(UserDto userDto, Integer userId) {
        if (!Objects.equals(userDto.id(), userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userDto.id()));
        }

        User userToUpdate = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId)));

        if (userDto.firstName() != null) {
            userToUpdate.setFirstName(userDto.firstName());
        }

        if (userDto.lastName() != null) {
            userToUpdate.setLastName(userDto.lastName());
        }

        if (userDto.patronymic() != null) {
            userToUpdate.setPatronymic(userDto.patronymic());
        }

        if (userDto.gender() != null) {
            if (userDto.gender().equals(Gender.MALE)) {
                userToUpdate.setGender(Gender.MALE);
            }
            if (userDto.gender().equals(Gender.FEMALE)) {
                userToUpdate.setGender(Gender.FEMALE);
            }
        }

        if (userDto.birthday() != null) {
            userToUpdate.setBirthday(userDto.birthday());
        }

        if (userDto.cityId() != null) {
            City city = cityRepository.findById(userDto.cityId()).orElseThrow(() ->
                    new CityNotFoundException(String.format("Город с id = %s не найден", userDto.cityId())));
            userToUpdate.setCity(city);
        }

        if (userDto.link() != null) {
            userToUpdate.setLink(userDto.link());
        }

        if (userDto.description() != null) {
            userToUpdate.setDescription(userDto.description());
        }

        if (userDto.nickname() != null) {
            userToUpdate.setNickname(userDto.nickname());
        }

        if (userDto.email() != null) {
            userToUpdate.setEmail(userDto.email());
        }

        if (userDto.phone() != null) {
            userToUpdate.setPhone(userDto.phone());
        }

        userRepository.save(userToUpdate);

        return String.format("Пользователь c id = %s обновлен", userId);
    }

    public String subscribeToUser(Integer subscriberId, Integer respondentId) {
        User subscriber = userRepository.findById(subscriberId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (подписчик) с id = %s не найден", subscriberId)));
        User respondent = userRepository.findById(respondentId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (респондент) с id = %s не найден", respondentId)));
        Subscription subscriptionForSave = new Subscription(subscriber, respondent);
        subscriptionRepository.save(subscriptionForSave);
        return String.format("Пользователь с id = %s подписался на пользователя с id = %s", subscriberId, respondentId);
    }

    public String unsubscribeToUser(Integer subscriberId, Integer respondentId) {
        User subscriber = userRepository.findById(subscriberId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (подписчик) с id = %s не найден", subscriberId)));
        User respondent = userRepository.findById(respondentId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (респондент) с id = %s не найден", respondentId)));
        Subscription subscriptionForDelete = subscriptionRepository.findBySubscriberAndRespondent(subscriber, respondent)
                .orElseThrow(() -> new SubscriptionNotFoundException(String.format("Пользователь с id = %s не " +
                        "подписан на пользователя с id = %s", subscriberId, respondentId)));
        subscriptionRepository.delete(subscriptionForDelete);
        return String.format("Пользователь с id = %s больше не подписан на пользователя с id = %s",
                subscriberId, respondentId);
    }
}
