package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewVersionPOJO {
    Long userId;
    String userToken;
    Long websiteId;
}
