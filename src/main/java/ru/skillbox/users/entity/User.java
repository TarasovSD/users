package ru.skillbox.users.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = "deletedUserFilter", condition = "deleted = :isDeleted")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String birthday;
    @ManyToOne
    private City city;
    private String link;
    private String description;
    private String nickname;
    private String email;
    private String phone;
    private boolean deleted = Boolean.FALSE;

    @ManyToMany(mappedBy = "users")
    private Set<HardSkill> hardSkills = new HashSet<>();

    public User() {
    }

    public User(Integer id, String firstName, String lastName, String patronymic, Gender gender, String birthday, City city,
                String link, String description, String nickname, String email, String phone) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return deleted == user.deleted && Objects.equals(getId(), user.getId()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getPatronymic(), user.getPatronymic()) && getGender() == user.getGender() && Objects.equals(getBirthday(), user.getBirthday()) && Objects.equals(getCity(), user.getCity()) && Objects.equals(getLink(), user.getLink()) && Objects.equals(getDescription(), user.getDescription()) && Objects.equals(getNickname(), user.getNickname()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPhone(), user.getPhone()) && Objects.equals(getHardSkills(), user.getHardSkills());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getPatronymic(), getGender(), getBirthday(), getCity(), getLink(), getDescription(), getNickname(), getEmail(), getPhone(), deleted, getHardSkills());
    }

    public Set<HardSkill> getHardSkills() {
        return hardSkills;
    }

    public void setHardSkills(Set<HardSkill> hardSkills) {
        this.hardSkills = hardSkills;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public City getCity() {
        return city;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addHardSkill(HardSkill hardSkill) {
        hardSkills.add(hardSkill);
    }
}
