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
}
