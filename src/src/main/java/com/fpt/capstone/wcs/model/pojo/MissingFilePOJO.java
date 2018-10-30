package com.fpt.capstone.wcs.model.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class MissingFilePOJO {
    private Integer type;

    public MissingFilePOJO(Integer type) {
        this.type = type;
    }
}
