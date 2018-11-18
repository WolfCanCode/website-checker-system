package com.fpt.capstone.wcs.model.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class RequestReportPOJO {
    private List<Long> listReportId;
    Long userId;
    String userToken;
    Long websiteId;
    Long pageOptionId;
}
