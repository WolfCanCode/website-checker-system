package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

@Data
@Getter
@Setter
public class RequestCommonPOJO {
    Long userId;
    String userToken;
    Long websiteId;
    Long pageOptionId;
    Long reportTime;

    public RequestCommonPOJO() {
    }

    public RequestCommonPOJO(Long userId, String userToken, Long websiteId, Long pageOptionId, Long reportTime) {
        this.userId = userId;
        this.userToken = userToken;
        this.websiteId = websiteId;
        this.pageOptionId = pageOptionId;
        this.reportTime = reportTime;
    }

    public RequestCommonPOJO(Long userId, String userToken, Long websiteId, Long pageOptionId) {
        this.userId = userId;
        this.userToken = userToken;
        this.websiteId = websiteId;
        this.pageOptionId = pageOptionId;
    }

    //    Long pageType;

//    public RequestCommonPOJO(Long userId, String userToken, Long websiteId, Long pageOptionId) {
//        this.userId = userId;
//        this.userToken = userToken;
//        this.websiteId = websiteId;
//        this.pageOptionId = pageOptionId;
//    }
}
