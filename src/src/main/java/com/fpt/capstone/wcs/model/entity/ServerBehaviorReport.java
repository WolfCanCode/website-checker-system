package com.fpt.capstone.wcs.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "ServerBehaviorReport")
    private List<Page> Page = new ArrayList<>();
}
