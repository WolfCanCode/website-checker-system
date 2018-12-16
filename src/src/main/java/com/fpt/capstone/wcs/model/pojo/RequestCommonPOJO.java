package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCommonPOJO {
    Long userId;
    String userToken;
    Long websiteId;
    Long pageOptionId;
//    Long pageType;

//    public RequestCommonPOJO(Long userId, String userToken, Long websiteId, Long pageOptionId) {
//        this.userId = userId;
//        this.userToken = userToken;
//        this.websiteId = websiteId;
//        this.pageOptionId = pageOptionId;
//    }
}
