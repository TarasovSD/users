package ru.skillbox.users.dto;

import ru.skillbox.users.entity.Gender;

public record UserFullDto(Integer id, String firstName, String lastName, String patronymic, Gender gender,
                          String birthday, CityForUserDto city, String link, String description, String nickname,
                          String email, String phone) {

    public record CityForUserDto(Integer id, String name) {
    }
}