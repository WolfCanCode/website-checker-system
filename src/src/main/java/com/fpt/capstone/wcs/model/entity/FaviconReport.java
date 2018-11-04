package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Getter
@Setter
@Entity
public class FaviconReport {

    @Id
    @GeneratedValue
    private long id;
    private String faviconUrl;
    private String webAddress;
    private String sizeFavicon;

    public FaviconReport(){
    }

    public  FaviconReport(String faviconUrl, String webAddress, String sizeFavicon){
        this.faviconUrl=faviconUrl;
        this.webAddress = webAddress;
        this.sizeFavicon  = sizeFavicon;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
