package com.reflectoring.csrf.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TOKEN")
public class Token implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String user;

    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
