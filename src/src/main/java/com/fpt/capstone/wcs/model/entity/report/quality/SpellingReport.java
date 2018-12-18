package com.fpt.capstone.wcs.model.entity.report.quality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@Table(name = "SpellingReport")
public class SpellingReport {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String wrongWord;

    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String excerpt;

    @NotNull

    private String page;


    private String decision; // ignore - fix


    private String selectedSuggestion; // if fix, keep selected word, else null

    private Date createdTime;
    private boolean delFlag = true;

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;
}
