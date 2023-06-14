package ru.skillbox.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.users.dto.CityDto;
import ru.skillbox.users.mapper.CityMapper;
import ru.skillbox.users.service.CityService;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    private final CityService cityService;

    final static Logger logger = LoggerFactory.getLogger(CityController.class);

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping()
    public String createCity(@RequestBody CityDto cityDto) {
        logger.info("Запрос на создание нового города отправлен в CityService");
        return cityService.createCity(CityMapper.toCity(cityDto));
    }

    @GetMapping("/{cityId}")
    public CityDto getCity(@PathVariable Integer cityId) {
        logger.info("Запрос на получение города с id = {} отправлен в CityService", cityId);
        return cityService.getCity(cityId);
    }

    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable Integer cityId) {
        logger.info("Запрос на удаление города с id = {} отправлен в CityService", cityId);
        cityService.deleteCity(cityId);
    }

    @PutMapping("/{cityId}")
    public String updateCity(@RequestBody CityDto cityDto, @PathVariable Integer cityId) {
        logger.info("Запрос на обновление города с id = {} отправлен в CityService", cityId);
        return cityService.updateCity(cityDto, cityId);
    }
}
