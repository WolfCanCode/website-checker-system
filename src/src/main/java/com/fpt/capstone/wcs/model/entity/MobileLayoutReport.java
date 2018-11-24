package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@Table(name = "MobileLayoutReport")
public class MobileLayoutReport {
    @Id
    @GeneratedValue
    private Long id;
    private String url;
    private String title;
    private String screenShot;
    private String issue;
    private Date createdTime;

    private boolean delFlag = true;

    public MobileLayoutReport(String url, String title, String screenShot, String issue) {
        this.url = url;
        this.title = title;
        this.screenShot = screenShot;
        this.issue = issue;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;
}
