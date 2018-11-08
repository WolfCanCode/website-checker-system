package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "Website")
public class Website {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String url;
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime modifiedTime;
    @JsonIgnore
    private boolean delFlag;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "website_user",
            joinColumns = { @JoinColumn(name = "web_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @JsonIgnore
    private List<User> user = new ArrayList<>();

    @OneToMany(targetEntity = Page.class, mappedBy = "website", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Page> page= new ArrayList<>();

    @OneToMany(targetEntity = Version.class, mappedBy = "website", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Version> version= new ArrayList<>();

    @OneToMany(targetEntity = PageOption.class, mappedBy = "website", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PageOption> pageOptions= new ArrayList<>();
}
