package ru.skillbox.users.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class HardSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    @ManyToMany
    @JoinTable(name = "users_hard_skills",
            joinColumns = @JoinColumn(name = "hard_skill_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public HardSkill() {
    }

    public HardSkill(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HardSkill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void addUser(User user) {
        users.add(user);
    }
}