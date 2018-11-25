package com.fpt.capstone.wcs.model.entity.report.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    private Date createdTime;
    private boolean delFlag = true;

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
