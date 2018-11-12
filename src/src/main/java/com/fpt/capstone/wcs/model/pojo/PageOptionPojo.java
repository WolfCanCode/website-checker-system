package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageOptionPojo {
    Long userId;
    String userToken;
    Long websiteId;
    List<Long> listPageId;
    String pageOptionName;
    Long pageOptionId;
}
