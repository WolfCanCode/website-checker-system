package com.fpt.capstone.wcs.model.entity.website;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@Table(name = "CookieData")
public class CookieData {
    @Id
    @GeneratedValue
    private Long id;
    private String cookieName;
    private String category;
    private String party;
    private String description;

    public CookieData(String cookieName, String category, String party, String description) {
        this.cookieName = cookieName;
        this.category = category;
        this.party = party;
        this.description = description;
    }
}
