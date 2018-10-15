package com.fpt.capstone.wcs.model;

import lombok.*;

@Data
@Getter
@Setter
public class SiteLink {
    private String srcUrl;
    private String desUrl;
    private int desType;

    public SiteLink(String srcUrl, String desUrl, int desType) {
        this.srcUrl = srcUrl;
        this.desUrl = desUrl;
        this.desType = desType;
    }
}