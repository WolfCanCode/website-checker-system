package com.fpt.capstone.wcs.model.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WebsitePojo {
    private long id;
    private String name;
    private String url;
    private int version;
    private String time;
}
