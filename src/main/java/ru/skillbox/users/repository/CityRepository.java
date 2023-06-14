package ru.skillbox.users.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillbox.users.entity.City;

import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Integer> {

    @Query("select c from City c where c.id = :id and c.deleted = false")
    Optional<City> findById(Integer id);
}
