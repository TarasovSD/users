package ru.skillbox.users.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillbox.users.entity.HardSkill;

public interface HardSkillRepository extends CrudRepository<HardSkill, Integer> {
}
