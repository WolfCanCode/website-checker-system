package com.fpt.capstone.wcs.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
public class SpeedTest {
    @Id
    @GeneratedValue
    private Long id;
    private String url;
    private String interactiveTime;
    private String pageLoadTime;
    private String size;

    public SpeedTest(String url, String interactiveTime, String pageLoadTime, String size) {
        this.url = url;
        this.interactiveTime = interactiveTime;
        this.pageLoadTime = pageLoadTime;
        this.size = size;
    }
}
