package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "Page")
public class Page {
    @Id @GeneratedValue
    private long id;
    @NotNull
    private int type;
    private String url;

    @ManyToOne()
    @JoinColumn(name="website_id")
    @JsonIgnore
    private Website website;

    @ManyToOne()
    @JoinColumn(name="ver_id")
    private Version version;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "broken_page_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<BrokenPageReport> BrokenPageReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "broken_link_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<BrokenLinkReport> BrokenLinkReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "contact_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<ContactReport> ContactReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "cookie_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<CookieReport> CookieReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "js_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<JavascriptReport> JavascriptReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "missing_file_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<MissingFileReport> MissingFileReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "redirection_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<RedirectionReport> RedirectionReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "server_behavior_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<ServerBehaviorReport> ServerBehaviorReport = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "speedtest_report_page",
            joinColumns = { @JoinColumn(name = "page_id") },
            inverseJoinColumns = { @JoinColumn(name = "report_id") })
    private List<SpeedTestReport> SpeedTestReport = new ArrayList<>();
}
