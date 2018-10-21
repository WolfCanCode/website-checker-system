package com.fpt.capstone.wcs.model;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
public class BrokenLink {

    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String httpMessage;
    private String urlPage;
    private String urlLink;


    public BrokenLink(int httpCode, String httpMessage,  String urlPage, String urlLink) {
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
        this.urlPage = urlPage;
        this.urlLink = urlLink;
    }
}
