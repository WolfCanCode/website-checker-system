package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SitemapValuePOJO {
    private String websiteUrl;
    private int version;
    private List<VerticePOJO> vertices;
}
