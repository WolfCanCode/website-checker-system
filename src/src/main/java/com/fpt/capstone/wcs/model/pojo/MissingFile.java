package com.fpt.capstone.wcs.model.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class MissingFile {
    private Integer type;

    public MissingFile(Integer type) {
        this.type = type;
    }
}
