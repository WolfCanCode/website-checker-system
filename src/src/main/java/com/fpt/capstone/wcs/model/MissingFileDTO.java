package com.fpt.capstone.wcs.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MissingFileDTO {
    private Integer type;

    public MissingFileDTO( Integer type) {
        this.type = type;
    }
}
