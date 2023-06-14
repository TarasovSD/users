package ru.skillbox.users.dto;

import ru.skillbox.users.entity.Gender;

public record UserDto(Integer id, String firstName, String lastName, String patronymic, Gender gender, String birthday,
                      Integer cityId, String link, String description, String nickname, String email, String phone) {
}