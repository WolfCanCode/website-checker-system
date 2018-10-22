package com.fpt.capstone.wcs.model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@Getter
@Setter

public class Cookie {
    @Id
    @GeneratedValue
    private Long id;
    private String cookieName;
    private String exampleValue;
    private String host;
    private Date expiryDate;

    public Cookie(String cookieName, String exampleValue, String host, Date expiryDate) {
        this.cookieName = cookieName;
        this.exampleValue = exampleValue;
        this.host = host;
        this.expiryDate = expiryDate;
    }
}
