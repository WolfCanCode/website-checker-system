package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "PageOption")
public class PageOption {
    @Id
    @GeneratedValue
    long id;
    String name;
    Date time;
    boolean delFlag=false;

    @NotNull
    @ManyToOne()
    @JoinColumn(name="website_id")
    private Website website;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "page_option_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "page_option_id") })
    @JsonIgnore
    private List<Page> pages = new ArrayList<>();

    @OneToMany(targetEntity = BrokenLinkReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BrokenLinkReport> brokenLinkReport= new ArrayList<>();

    @OneToMany(targetEntity = BrokenPageReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BrokenPageReport> brokenPageReport= new ArrayList<>();

    @OneToMany(targetEntity = ContactReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ContactReport> contactReport= new ArrayList<>();

    @OneToMany(targetEntity = CookieReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CookieReport> cookieReport= new ArrayList<>();

    @OneToMany(targetEntity = JavascriptReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<JavascriptReport> javascriptReport= new ArrayList<>();

    @OneToMany(targetEntity = MissingFileReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MissingFileReport> missingFileReport= new ArrayList<>();

    @OneToMany(targetEntity = RedirectionReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RedirectionReport> redirectionReport= new ArrayList<>();

    @OneToMany(targetEntity = ServerBehaviorReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ServerBehaviorReport> serverBehaviorReport= new ArrayList<>();

    @OneToMany(targetEntity = SpeedTestReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SpeedTestReport> speedTestReport= new ArrayList<>();

    @OneToMany(targetEntity = FaviconReport.class, mappedBy = "pageOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FaviconReport> faviconReport= new ArrayList<>();
}
