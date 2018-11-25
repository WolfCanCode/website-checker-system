package com.fpt.capstone.wcs.model.entity.report.quality;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@Table(name = "ProhibitedContentReport")
public class ProhibitedContentReport {
    @Id
    @GeneratedValue
    private Long id;
    private String urlPage;
    private String word;
    private String fragment;
    private String type;
    private Date createdTime;
    private boolean delFlag = true;


    public ProhibitedContentReport(String urlPage, String word,String fragment, String type) {
        this.urlPage = urlPage;
        this.word = word;
        this.fragment = fragment;
        this.type = type;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;
}
