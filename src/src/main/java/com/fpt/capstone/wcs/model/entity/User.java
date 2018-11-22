package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Setter
@Getter
@Table(name="User")
@JsonIgnoreProperties( value = {"password","token"}, allowSetters= true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String name;
    @JsonProperty("password")
    private String password;
    private String email;
    @JsonProperty("token")
    private String token;
    @JsonIgnore
    private boolean delFlag=false;

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

    @OneToMany(targetEntity = User.class, mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> staff= new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name="manager_id")
    private User manager;



    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "user")
    private List<Website> website = new ArrayList<>();

    @OneToMany(targetEntity = PageOption.class, mappedBy = "createdUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PageOption> pageOptions= new ArrayList<>();

}
