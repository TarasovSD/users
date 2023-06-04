package ru.skillbox.users.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.users.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
