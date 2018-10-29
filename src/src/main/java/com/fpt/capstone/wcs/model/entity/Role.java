package com.fpt.capstone.wcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "Role")

public class Role {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    @OneToMany(targetEntity = User.class, mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> user= new ArrayList<>();

}
