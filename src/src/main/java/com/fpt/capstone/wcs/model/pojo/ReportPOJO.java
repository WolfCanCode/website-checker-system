package com.fpt.capstone.wcs.model.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class ReportPOJO {
    private Long reportDate;
    private String websiteName;
    private long websiteId;
    private String pageOptionName;
    private long pageOptionId;
    private long userId;
    private String userName;

}
