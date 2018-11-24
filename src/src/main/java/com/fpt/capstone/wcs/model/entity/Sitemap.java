package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "sitemap")
public class Sitemap {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String map;

    @NotNull
    private String typeMap;

    @NotNull
    private String urlMap;

    @ManyToOne()
    @JoinColumn(name="website_id")
    @JsonIgnore
    private Website website;

    @OneToMany(targetEntity = Version.class, mappedBy = "sitemap_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Version> version= new ArrayList<>();
}
