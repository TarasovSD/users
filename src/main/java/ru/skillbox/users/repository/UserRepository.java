package ru.skillbox.users.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillbox.users.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("select u from User u where u.id = :id and u.deleted = false")
    Optional<User> findById(Integer id);
}
