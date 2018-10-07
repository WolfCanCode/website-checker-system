package com.fpt.capstone.wcs.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
@Getter
@Setter
public class SpeedTest {
    @Id
    @GeneratedValue
    public Long id;
    public String url;
    public String interactiveTime;
    public String pageLoadTime;
    public String size;


    public SpeedTest(String url, String interactiveTime, String pageLoadTime, String size) {
        this.interactiveTime = interactiveTime;
        this.pageLoadTime = pageLoadTime;
        this.size = size;
        this.url = url;
    }
}
