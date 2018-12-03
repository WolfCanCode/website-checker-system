package com.fpt.capstone.wcs.model.entity.report.content;

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
@Table(name = "ContactReport")
public class ContactReport {
    @Id
    @GeneratedValue
    private long id;
    private String phoneMail;
    private String url;

    private String type;
    private Date createdTime;
    private boolean delFlag = true;
    public ContactReport(String phoneMail, String url , String type){
        this.phoneMail=phoneMail;

        this.url=url;
        this.type=type;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
