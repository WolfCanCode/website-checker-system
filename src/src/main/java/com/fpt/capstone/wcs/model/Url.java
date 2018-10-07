package com.fpt.capstone.wcs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class Url {
    public String url;

    public Url(){}

    public Url(String url) {
        this.url = url;
    }


}
