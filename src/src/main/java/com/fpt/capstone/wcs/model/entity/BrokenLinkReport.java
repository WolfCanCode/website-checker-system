package com.fpt.capstone.wcs.model.entity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "BrokenLinkReport")
public class BrokenLinkReport {

    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String httpMessage;
    private String urlPage;
    private String urlLink;


    public BrokenLinkReport(int httpCode, String httpMessage, String urlPage, String urlLink) {
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
        this.urlPage = urlPage;
        this.urlLink = urlLink;
    }


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "BrokenLinkReport")
    private List<Page> Page = new ArrayList<>();


}
