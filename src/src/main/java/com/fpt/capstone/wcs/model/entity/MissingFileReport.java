package com.fpt.capstone.wcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@Entity
@Table(name="MissingFileReport")
public class MissingFileReport {
    @Id
    @GeneratedValue
    private long id;
    private String fileMissing;
    private String description;
    private String pages;

    public MissingFileReport(String fileMissing, String description, String pages) {
        this.fileMissing = fileMissing;
        this.description = description;
        this.pages = pages;
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "MissingFileReport")
    private List<Page> Page = new ArrayList<>();
}
