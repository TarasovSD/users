package ru.skillbox.users.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.users.entity.Subscription;
import ru.skillbox.users.entity.User;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

    List<Subscription> findAllBySubscriber(User user);
}
