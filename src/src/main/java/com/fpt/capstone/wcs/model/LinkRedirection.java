package com.fpt.capstone.wcs.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
public class LinkRedirection {
    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String url;
    private String type;
    private String driectToUrl;

    public LinkRedirection(int httpCode, String url, String type, String driectToUrl) {
        this.httpCode = httpCode;
        this.url = url;
        this.type = type;
        this.driectToUrl = driectToUrl;
    }
}
