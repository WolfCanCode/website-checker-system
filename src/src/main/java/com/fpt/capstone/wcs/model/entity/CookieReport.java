package com.fpt.capstone.wcs.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Getter
@Setter
@Table(name="CookieReport")
public class CookieReport {
    @Id
    @GeneratedValue
    private Long id;
    private String cookieName;
    private String category;
    private String party;
    private String description;
    private Date createdTime;

    private boolean delFlag = true;

    public CookieReport(String cookieName, String category, String party, String description) {
        this.cookieName = cookieName;
        this.category = category;
        this.party = party;
        this.description = description;
    }

    public CookieReport(String cookieName, String party) {
        this.cookieName = cookieName;
        this.party = party;
    }

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
