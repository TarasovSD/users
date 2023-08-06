package ru.skillbox.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.skillbox.users.dto.CityDto;
import ru.skillbox.users.entity.City;
import ru.skillbox.users.exception.CityNotFoundException;
import ru.skillbox.users.mapper.CityMapper;
import ru.skillbox.users.repository.CityRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CityServiceTest {

    CityRepository cityRepository;
    CityService cityService;
    City city;
    City cityForSave;
    CityDto cityDto;

    @BeforeEach
    void init() {
        cityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityService(cityRepository);
        cityForSave = new City(null, "Moscow");
        city = new City(1, "Moscow");
        cityDto = CityMapper.toCityDto(city);
    }

    @Test
    public void createCity() {
        Mockito.when(cityRepository.save(cityForSave)).thenReturn(city);

        CityDto cityForCheck = cityService.createCity(cityForSave);

        assertEquals(cityDto, cityForCheck);

        verify(cityRepository, times(1))
                .save(any());
    }

    @Test
    public void getCity() {
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.ofNullable(city));

        CityDto cityForCheck = cityService.getCity(1);

        assertEquals(cityDto, cityForCheck);

        verify(cityRepository, times(1))
                .findById(1);

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> cityService.getCity(anyInt()));
    }

    @Test
    public void deleteCity() {
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.ofNullable(city));

        cityService.deleteCity(1);

        verify(cityRepository, times(1))
                .findById(1);

        verify(cityRepository, times(1))
                .delete(city);

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> cityService.deleteCity(1));
    }

    @Test
    public void updateCity() {
        CityDto citiDtoForUpdate = new CityDto(1, "Saint Petersburg");
        City updatedCity = new City(1, "Saint Petersburg");

        Mockito.when(cityRepository.save(any())).thenReturn(updatedCity);
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.ofNullable(city));

        CityDto cityAfterUpdate = cityService.updateCity(citiDtoForUpdate, 1);

        assertEquals(cityAfterUpdate, citiDtoForUpdate);

        verify(cityRepository, times(1))
                .findById(1);
        verify(cityRepository, times(1))
                .save(any());

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> cityService.getCity(1));
    }
}