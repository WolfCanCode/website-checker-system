package com.fpt.capstone.wcs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data

@Getter
@Setter
public class LinkRedirection {
    @Id
    @GeneratedValue
    public Long id;
    private int httpCode;
    private String url;
    private String type;
    private String driectToUrl;
    public LinkRedirection(String url, String driectToUrl , int httpCode,  String type){
        this.url = url;
        this.driectToUrl=driectToUrl;
        this.httpCode= httpCode;
        this.type = type;
    }
}
