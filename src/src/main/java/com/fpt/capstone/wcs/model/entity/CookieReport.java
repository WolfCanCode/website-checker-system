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
    private String exampleValue;
    private String host;
    private Date expiryDate;

    public CookieReport(String cookieName, String exampleValue, String host, Date expiryDate) {
        this.cookieName = cookieName;
        this.exampleValue = exampleValue;
        this.host = host;
        this.expiryDate = expiryDate;
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "CookieReport")
    private List<Page> Page = new ArrayList<>();
}
