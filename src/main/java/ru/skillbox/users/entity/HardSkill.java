package ru.skillbox.users.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hard_skills")
@SQLDelete(sql = "UPDATE hard_skills SET deleted = true WHERE id=?")
@FilterDef(name = "deletedSkillFilter", parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = "deletedSkillFilter", condition = "deleted = :isDeleted")
public class HardSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private boolean deleted = Boolean.FALSE;

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
