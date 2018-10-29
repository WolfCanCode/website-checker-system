package com.fpt.capstone.wcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@Entity
@Table(name = "ContactReport")
public class ContactReport {
    @Id
    @GeneratedValue
    private long id;
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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "ContactReport")
    private List<Page> Page = new ArrayList<>();
}
