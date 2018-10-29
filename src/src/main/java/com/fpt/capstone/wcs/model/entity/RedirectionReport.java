package com.fpt.capstone.wcs.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@Table(name="RedirectionReport")
public class RedirectionReport {
    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String url;
    private String type;
    private String driectToUrl;

    public RedirectionReport(int httpCode, String url, String type, String driectToUrl) {
        this.httpCode = httpCode;
        this.url = url;
        this.type = type;
        this.driectToUrl = driectToUrl;
    }
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "RedirectionReport")
    private List<Page> Page = new ArrayList<>();
}
