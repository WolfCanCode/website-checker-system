package com.fpt.capstone.wcs.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@Table(name="User")
public class User {
    @Id
    @GeneratedValue
    public Long id;
    public String name;
    public String password;
    public String email;
    public String token;

    public User(Long id, String token)
    {
        this.id = id;
        this.token = token;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    @ManyToOne()
    @JoinColumn(name="role_id")
    private Role role;

}
