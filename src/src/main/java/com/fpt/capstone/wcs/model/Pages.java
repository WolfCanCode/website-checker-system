package com.fpt.capstone.wcs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Pages {
    @Id
    @GeneratedValue
    public Long id;
    private int httpCode;
    private String url;
    private String titleWeb;
    private String canonicalUrl;



    public Pages (String url, String titleWeb, String canonicalUrl, int  httpCode){
        this.url=url;
        this.httpCode=httpCode;
        this.titleWeb = titleWeb;
        this.canonicalUrl = canonicalUrl;
    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setCanonicalUrl(String canonicalUrl) {
//        this.canonicalUrl = canonicalUrl;
//    }
//
//    public String getCanonicalUrl() {
//        return canonicalUrl;
//    }
//
//    public void setHttpCode(String httpCode) {
//        this.httpCode = httpCode;
//    }
//
//    public String getHttpCode() {
//        return httpCode;
//    }
//
//    public void setTitleWeb(String titleWeb) {
//        this.titleWeb = titleWeb;
//    }
//
//    public String getTitleWeb() {
//        return titleWeb;
//    }
}
