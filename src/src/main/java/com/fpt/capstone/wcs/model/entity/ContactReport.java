package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "ContactReport")
public class ContactReport {
    @Id
    @GeneratedValue
    private String phoneMail;
    private String url;
    private String position;
    private String type;
    public ContactReport(String phoneMail, String url, String position, String type){
        this.phoneMail=phoneMail;
        this.position = position;
        this.url=url;
        this.type=type;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
