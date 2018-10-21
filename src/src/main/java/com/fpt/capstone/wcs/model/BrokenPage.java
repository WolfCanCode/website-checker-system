package com.fpt.capstone.wcs.model;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
public class BrokenPage {
    @Id
    @GeneratedValue
    private Long id;
    private String urlPage;
    private String stt;
    private int httpCode;
    private String httpMessage;

    public BrokenPage(String urlPage, String stt, int httpCode, String httpMessage) {
        this.urlPage = urlPage;
        this.stt = stt;
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
    }
}
