package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpellingSuggestionRequestPOJO {
    Long userId;
    String userToken;
    Long websiteId;
    Long pageOptionId;
    String wrongWord;
}
