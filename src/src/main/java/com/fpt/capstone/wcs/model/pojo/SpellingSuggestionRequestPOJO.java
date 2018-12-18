package com.fpt.capstone.wcs.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class SpellingSuggestionRequestPOJO {
    Long userId;
    String userToken;
    Long websiteId;
    Long pageOptionId;
    String wrongWord;
}
