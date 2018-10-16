package com.fpt.capstone.wcs.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
public class JSInfo {
    @Id @GeneratedValue
    private long id;
    @Column(length = 10000)
    private String messages;
    private String type;
    private String page;

    public JSInfo(String messages, String type, String page) {
        this.messages = messages;
        this.type = type;
        this.page = page;
    }
}
