package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name="RedirectionReport")
public class RedirectionReport {
    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String url;
    private String type;
    private String driectToUrl;

    public RedirectionReport(int httpCode, String url, String type, String driectToUrl) {
        this.httpCode = httpCode;
        this.url = url;
        this.type = type;
        this.driectToUrl = driectToUrl;
    }
    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
