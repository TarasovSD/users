package ru.skillbox.users.entity;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String gender;
    private String birthday;
    @ManyToOne
    private City city;
    private String link;
    private String description;
    private String nickname;
    private String email;
    private String phone;
    private boolean deleted = Boolean.FALSE;

    @ManyToMany (mappedBy = "users")
    private Set<HardSkill> hardSkills = new HashSet<>();

    public User() {
    }

    public User(String firstName, String lastName, String patronymic, String gender, String birthday, City city, String link, String description, String nickname, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.gender = gender;
        this.birthday = birthday;
        this.city = city;
        this.link = link;
        this.description = description;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", city=" + city +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public Set<HardSkill> getHardSkills() {
        return hardSkills;
    }

    public void setHardSkills(Set<HardSkill> hardSkills) {
        this.hardSkills = hardSkills;
    }

    public void addHardSkill(HardSkill hardSkill) {
        hardSkills.add(hardSkill);
    }
}
