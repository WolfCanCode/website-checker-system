package com.fpt.capstone.wcs.model.entity.report.quality;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Getter
@Setter
@Table(name = "BrokenPageReport")

public class BrokenPageReport {
    @Id
    @GeneratedValue
    private Long id;
    private String urlPage;
    private String stt;
    private int httpCode;
    private String httpMessage;
    private Date createdTime;
    private boolean delFlag = true;

    public BrokenPageReport(String urlPage, String stt, int httpCode, String httpMessage) {
        this.urlPage = urlPage;
        this.stt = stt;
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
    }


    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;


}
