package com.fpt.capstone.wcs.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="ServerBehaviorReport")
public class ServerBehaviorReport {
    @Id @GeneratedValue
    private long id;
    private boolean isRedirectWWW;
    private boolean isAllPageSSL;
    private boolean isExistErrorPage;
    private boolean isRedirectHTTPS;

    @ManyToOne()
    @JoinColumn(name="page_option_id")
    @JsonIgnore
    private PageOption pageOption;

}
