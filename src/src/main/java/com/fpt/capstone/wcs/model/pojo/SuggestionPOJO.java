package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionPOJO {
    String suggestWord;
    String type;
    String definition;
}
