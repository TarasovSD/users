package ru.skillbox.users.service;

import org.springframework.stereotype.Service;
import ru.skillbox.users.dto.UserDto;
import ru.skillbox.users.dto.UserFullDto;
import ru.skillbox.users.entity.City;
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
import java.util.Optional;

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

    public UserFullDto createUser(UserDto userDto) {
        Integer cityId = userDto.cityId();
        City city = cityRepository.findById(cityId).orElseThrow(() ->
                new CityNotFoundException(String.format("Город с id = %s не найден", cityId)));
        User user = UserMapper.toUser(userDto, city);
        return UserMapper.toUserFullDto(userRepository.save(user));
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

    public UserFullDto updateUser(UserDto userDto, Integer userId) {
        if (!Objects.equals(userDto.id(), userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userDto.id()));
        }

        User userToUpdate = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId)));

        Optional.ofNullable(userDto.firstName())
                .ifPresent(userToUpdate::setFirstName);

        Optional.ofNullable(userDto.lastName())
                .ifPresent(userToUpdate::setLastName);

        Optional.ofNullable(userDto.patronymic())
                .ifPresent(userToUpdate::setPatronymic);

        Optional.ofNullable(userDto.gender())
                .ifPresent(userToUpdate::setGender);

        Optional.ofNullable(userDto.birthday())
                .ifPresent(userToUpdate::setBirthday);

        Optional.ofNullable(cityRepository.findById(userDto.cityId()))
                .ifPresent(city -> userToUpdate.setCity(city.orElseThrow(() ->
                        new CityNotFoundException(String.format("Город с id = %s не найден", userDto.cityId())))));

        Optional.ofNullable(userDto.link())
                .ifPresent(userToUpdate::setLink);

        Optional.ofNullable(userDto.description())
                .ifPresent(userToUpdate::setDescription);

        Optional.ofNullable(userDto.nickname())
                .ifPresent(userToUpdate::setNickname);

        Optional.ofNullable(userDto.email())
                .ifPresent(userToUpdate::setEmail);

        if (userDto.phone() != null) {
            userToUpdate.setPhone(userDto.phone());
        }

        Optional.ofNullable(userDto.phone())
                .ifPresent(userToUpdate::setPhone);

        return UserMapper.toUserFullDto(userRepository.save(userToUpdate));
    }

    public void subscribeToUser(Integer subscriberId, Integer respondentId) {
        User subscriber = userRepository.findById(subscriberId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (подписчик) с id = %s не найден", subscriberId)));
        User respondent = userRepository.findById(respondentId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (респондент) с id = %s не найден", respondentId)));
        Subscription subscriptionForSave = new Subscription(subscriber, respondent);
        subscriptionRepository.save(subscriptionForSave);
    }

    public void unsubscribeFromUser(Integer subscriberId, Integer respondentId) {
        User subscriber = userRepository.findById(subscriberId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (подписчик) с id = %s не найден", subscriberId)));
        User respondent = userRepository.findById(respondentId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь (респондент) с id = %s не найден", respondentId)));
        Subscription subscriptionForDelete = subscriptionRepository.findBySubscriberAndRespondent(subscriber, respondent)
                .orElseThrow(() -> new SubscriptionNotFoundException(String.format("Пользователь с id = %s не " +
                        "подписан на пользователя с id = %s", subscriberId, respondentId)));
        subscriptionRepository.delete(subscriptionForDelete);
    }
}
