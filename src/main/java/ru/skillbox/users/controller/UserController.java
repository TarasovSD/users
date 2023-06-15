package ru.skillbox.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.users.dto.UserDto;
import ru.skillbox.users.dto.UserFullDto;
import ru.skillbox.users.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserFullDto createUser(@RequestBody UserDto userDto) {
        logger.info("Запрос на создание нового пользователя отправлен в UserService");
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserFullDto getUser(@PathVariable Integer userId) {
        logger.info("Запрос на получение пользователя с id = {} отправлен в UserService", userId);
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        logger.info("Запрос на удаление пользователя с id = {} отправлен в UserService", userId);
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public UserFullDto updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) {
        logger.info("Запрос на обновление пользователя с id = {} отправлен в UserService", userId);
        return userService.updateUser(userDto, userId);
    }

    @PostMapping("/{subscriberId}/subscribe/{respondentId}")
    public void subscribeToUser(@PathVariable Integer subscriberId, @PathVariable Integer respondentId) {
        logger.info("Запрос на подписку пользователем с id = {} на пользователя с id = {} отправлен в UserService",
                subscriberId, respondentId);
        userService.subscribeToUser(subscriberId, respondentId);
    }

    @DeleteMapping("/{subscriberId}/unsubscribe/{respondentId}")
    public void unsubscribeToUser(@PathVariable Integer subscriberId, @PathVariable Integer respondentId) {
        logger.info("Запрос на отдписку пользователем с id = {} от пользователя с id = {} отправлен в UserService",
                subscriberId, respondentId);
        userService.unsubscribeToUser(subscriberId, respondentId);
    }
}
