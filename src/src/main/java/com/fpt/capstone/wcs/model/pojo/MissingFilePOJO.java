package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

import java.util.List;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MissingFilePOJO {
    private List<Integer> listType;
    private Long userId;
    private String userToken;
    private Long websiteId;
    private Long pageOptionId;
    
}
