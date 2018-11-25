package com.fpt.capstone.wcs.model.entity.report.quality;

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
@Table(name = "GrammarReport")
public class GrammarReport {
    @Id
    @GeneratedValue
    private long id;
    private String word;
    private String reason;
    private String solution;
    private String suggestion;
    private Date createdTime;
    private boolean delFlag = true;

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;
}
