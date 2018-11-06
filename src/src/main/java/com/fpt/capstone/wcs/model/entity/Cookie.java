package com.fpt.capstone.wcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
public class Cookie {
    @Id
    @GeneratedValue
    private Long id;
    private String cookieName;
    private String category;
    private String party;
    private String description;

    public Cookie(String cookieName, String category, String party, String description) {
        this.cookieName = cookieName;
        this.category = category;
        this.party = party;
        this.description = description;
    }
}
