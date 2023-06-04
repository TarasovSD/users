package ru.skillbox.users.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.users.entity.City;

public interface CityRepository extends CrudRepository<City, Integer> {
}
