package com.fpt.capstone.wcs.model.pojo;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerticePOJO {
    private String url;
    private List<LinkPOJO> refTo;
    private List<LinkPOJO> refBy;

}
