package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SiteLinkPOJO {
    private String srcUrl;
    private String desUrl;
    private int desType;
}