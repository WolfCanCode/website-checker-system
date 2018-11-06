package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "Page")
public class Page {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private int type;
    private String url;

    @ManyToOne()
    @JoinColumn(name="website_id")
    @JsonIgnore
    private Website website;

    @ManyToOne()
    @JoinColumn(name="ver_id")
    private Version version;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "pages")
    private List<PageOption> pageOptions = new ArrayList<>();


}
