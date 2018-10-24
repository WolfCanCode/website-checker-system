package com.fpt.capstone.wcs.model.entity;


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
    @Column(length = 256)
    private String messages;
    private String type;
    private String page;

    public JavascriptReport(String messages, String type, String page) {
        this.messages = messages;
        this.type = type;
        this.page = page;
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "JavascriptReport")
    private List<Page> Page = new ArrayList<>();
}
