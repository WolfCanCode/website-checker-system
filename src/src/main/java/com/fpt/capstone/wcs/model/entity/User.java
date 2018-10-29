package com.fpt.capstone.wcs.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Setter
@Getter
@Table(name="User")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String password;
    private String email;
    private String token;

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


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "User")
    private List<Website> Website = new ArrayList<>();

}
