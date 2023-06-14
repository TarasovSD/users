package ru.skillbox.users.service;

import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.skillbox.users.dto.CityDto;
import ru.skillbox.users.entity.City;
import ru.skillbox.users.exception.CityNotFoundException;
import ru.skillbox.users.mapper.CityMapper;
import ru.skillbox.users.repository.CityRepository;

@Service
@PersistenceContext
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public String createCity(City city) {
        City createdCity = cityRepository.save(city);
        return String.format("Город %s c id = %s сохранен в базу данных",
                createdCity.getName(), createdCity.getId());
    }

    public CityDto getCity(Integer cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(() ->
                new CityNotFoundException(String.format("Город c id = %s не найден", cityId)));
        return CityMapper.toCityDto(city);
    }

    public void deleteCity(Integer cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(() ->
                new CityNotFoundException(String.format("Город c id = %s не найден", cityId)));
        cityRepository.delete(city);
    }

    public String updateCity(@NotNull CityDto cityDto, Integer cityId) {
        City cityToUpdate = CityMapper.toCity(getCity(cityId));
        cityToUpdate.setName(cityDto.name());
        cityRepository.save(cityToUpdate);
        return String.format("Город c id = %s обновлен", cityId);
    }
}
