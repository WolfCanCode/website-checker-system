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
@Table(name = "BrokenLinkReport")
public class BrokenLinkReport {

    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String httpMessage;
    private String urlPage;
    private String urlLink;
    private Date createdTime;
    private boolean delFlag = true;


    public BrokenLinkReport(int httpCode, String httpMessage, String urlPage, String urlLink) {
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
        this.urlPage = urlPage;
        this.urlLink = urlLink;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
