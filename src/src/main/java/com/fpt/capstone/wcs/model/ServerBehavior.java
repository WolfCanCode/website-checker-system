package com.fpt.capstone.wcs.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ServerBehavior {
    @Id @GeneratedValue
    private long id;
    private boolean isRedirectWWW;
    private boolean isAllPageSSL;
    private boolean isExistErrorPage;
    private boolean isRedirectHTTPS;
}
