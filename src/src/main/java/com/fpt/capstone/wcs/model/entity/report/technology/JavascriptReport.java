package com.fpt.capstone.wcs.model.entity.report.technology;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Getter
@Setter
@Table(name="JavascriptReport")
public class JavascriptReport{
    @Id
    @GeneratedValue
    private long id;
    @Column(length = 1000)
    private String messages;
    private String type;
    private String pages;
    private Date createdTime;
    private boolean delFlag = true;

    public JavascriptReport(String messages, String type, String pages) {
        this.messages = messages;
        this.type = type;
        this.pages = pages;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
