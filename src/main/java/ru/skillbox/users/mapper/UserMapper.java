package ru.skillbox.users.mapper;

import ru.skillbox.users.dto.UserDto;
import ru.skillbox.users.dto.UserFullDto;
import ru.skillbox.users.entity.City;
import ru.skillbox.users.entity.User;

public class UserMapper {

    public static User toUser(UserDto userDto, City city) {
        return new User(userDto.id(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.patronymic(),
                userDto.gender(),
                userDto.birthday(),
                city,
                userDto.link(),
                userDto.description(),
                userDto.nickname(),
                userDto.email(),
                userDto.phone());
    }

    public static UserFullDto toUserFullDto(User user) {
        return new UserFullDto(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getGender(),
                user.getBirthday(),
                new UserFullDto.CityForUserDto(user.getCity().getId(), user.getCity().getName()),
                user.getLink(),
                user.getDescription(),
                user.getNickname(),
                user.getEmail(),
                user.getPhone()
        );
    }
}
