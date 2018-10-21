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
public class MissingFilesPages {
    @Id
    @GeneratedValue
    private long id;
    private String fileMissing;
    private String description;
    private String pages;

    public MissingFilesPages(String fileMissing, String description, String pages) {
        this.fileMissing = fileMissing;
        this.description = description;
        this.pages = pages;
    }
}
