package com.fpt.capstone.wcs.model.entity.website;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.user.Website;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @OneToOne()
    @JoinColumn(name="ver_id")
    private Version version;
}
