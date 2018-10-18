package com.fpt.capstone.wcs.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Getter
@Setter
@Entity
public class ContactDetail {
    @Id
    @GeneratedValue
    private long id;
    private String phoneMail;
    private String url;
    private String position;
    private String type;
    public ContactDetail(String phoneMail, String url, String position, String type){
        this.phoneMail=phoneMail;
        this.position = position;
        this.url=url;
        this.type=type;
    }
}
