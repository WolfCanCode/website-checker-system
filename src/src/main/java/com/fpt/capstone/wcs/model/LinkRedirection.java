package com.fpt.capstone.wcs.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkRedirection {
    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String url;
    private String type;
    private String driectToUrl;
}
