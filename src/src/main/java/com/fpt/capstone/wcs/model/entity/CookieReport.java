package com.fpt.capstone.wcs.model.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Getter
@Setter
@Table(name="CookieReport")
public class CookieReport {
    @Id
    @GeneratedValue
    private Long id;
    private String cookieName;
    private String category;
    private String party;
    private String description;

    public CookieReport(String cookieName, String category, String party, String description) {
        this.cookieName = cookieName;
        this.category = category;
        this.party = party;
        this.description = description;
    }

    public CookieReport(String cookieName, String party) {
        this.cookieName = cookieName;
        this.party = party;
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "CookieReport")
    private List<Page> Page = new ArrayList<>();
}
