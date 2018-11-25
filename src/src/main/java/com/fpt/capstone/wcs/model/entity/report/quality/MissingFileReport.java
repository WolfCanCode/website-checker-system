package com.fpt.capstone.wcs.model.entity.report.quality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@Setter
@Entity
@Table(name="MissingFileReport")
public class MissingFileReport {
    @Id
    @GeneratedValue
    private long id;
    private String fileMissing;
    private String description;
    private String pages;
    private Date createdTime;
    private boolean delFlag = true;

    public MissingFileReport(String fileMissing, String description, String pages) {
        this.fileMissing = fileMissing;
        this.description = description;
        this.pages = pages;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
