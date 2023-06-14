package ru.skillbox.users.mapper;

import ru.skillbox.users.dto.CityDto;
import ru.skillbox.users.entity.City;

public class CityMapper {

    public static City toCity(CityDto cityDto) {
        return new City(cityDto.id(),
                cityDto.name());
    }

    public static CityDto toCityDto(City city) {
        return new CityDto(city.getId(),
                city.getName());
    }
}
