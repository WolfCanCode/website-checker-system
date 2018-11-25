package com.fpt.capstone.wcs.model.entity.report.technology;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="ServerBehaviorReport")
public class ServerBehaviorReport {
    @Id
    @GeneratedValue
    private long id;
    private boolean isRedirectWWW;
    private boolean isPageSSL;
    private boolean isRedirectHTTPS;
    private Date createdTime;
    private boolean delFlag = true;
    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
