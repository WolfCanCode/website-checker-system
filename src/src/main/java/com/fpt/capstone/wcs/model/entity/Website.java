package com.fpt.capstone.wcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@Table(name = "Website")
public class Website {
    @Id @GeneratedValue
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String url;
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime modifiedTime;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "website_page",
            joinColumns = { @JoinColumn(name = "web_id") },
            inverseJoinColumns = { @JoinColumn(name = "page_id") })
    private List<Page> Page = new ArrayList<>();
}
