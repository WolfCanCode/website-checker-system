package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferencePOJO {
    //    RequestCommonPOJO requestCommonPOJO;
    Long userId;
    String userToken;
    Long websiteId;
    Long pageOptionId;
    String url;


}
