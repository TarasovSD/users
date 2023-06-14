package ru.skillbox.users.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@Entity
@Table(name = "cities")
@SQLDelete(sql = "UPDATE cities SET deleted = true WHERE id=?")
@FilterDef(name = "deletedCityFilter", parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = "deletedCityFilter", condition = "deleted = :isDeleted")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    private boolean deleted = Boolean.FALSE;

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public City(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}