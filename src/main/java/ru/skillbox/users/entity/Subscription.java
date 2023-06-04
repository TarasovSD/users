package ru.skillbox.users.entity;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private User subscriber;
    @ManyToOne
    private User respondent;

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
