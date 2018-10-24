package com.fpt.capstone.wcs.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Getter
@Setter
@Table(name="PageReport")
public class Pages {
    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String url;
    private String titleWeb;
    private String canonicalUrl;

    public Pages(int httpCode, String url, String titleWeb, String canonicalUrl) {
        this.httpCode = httpCode;
        this.url = url;
        this.titleWeb = titleWeb;
        this.canonicalUrl = canonicalUrl;
    }
}
