package ru.skillbox.users.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@Entity
@Table(name = "subscriptions")
@SQLDelete(sql = "UPDATE subscriptions SET deleted = true WHERE id=?")
@FilterDef(name = "deletedSubscriptionFilter", parameters = @ParamDef(name = "isDeleted", type = BooleanJavaType.class))
@Filter(name = "deletedSubscriptionFilter", condition = "deleted = :isDeleted")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private User subscriber;
    @ManyToOne
    private User respondent;
    private boolean deleted = Boolean.FALSE;

    public Subscription() {
    }

    public Subscription(User subscriber, User respondent) {
        this.subscriber = subscriber;
        this.respondent = respondent;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", subscriber=" + subscriber +
                ", respondent=" + respondent +
                '}';
    }
}
