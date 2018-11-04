package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@Table(name="PageReport")
public class PageReport {
    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String url;
    private String titleWeb;
    private String canonicalUrl;

    public PageReport(int httpCode, String url, String titleWeb, String canonicalUrl) {
        this.httpCode = httpCode;
        this.url = url;
        this.titleWeb = titleWeb;
        this.canonicalUrl = canonicalUrl;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;
}
