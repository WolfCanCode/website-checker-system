package com.fpt.capstone.wcs.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    public Long id;
    public String name;
    public String password;
    public String email;
    public String token;

    public User(String name, String password, String email, String token) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.token = token;
    }
}
