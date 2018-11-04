package com.fpt.capstone.wcs.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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
