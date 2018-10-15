package com.fpt.capstone.wcs.model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    public Long id;
    private int httpCode;
    private String urlPage;
    private String urlLink;



    public BrokenLink(int httpCode, String urlPage, String urlLink) {
        this.httpCode = httpCode;
        this.urlPage = urlPage;
        this.urlLink = urlLink;
    }
}
