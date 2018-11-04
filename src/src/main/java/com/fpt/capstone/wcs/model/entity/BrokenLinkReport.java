package com.fpt.capstone.wcs.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
