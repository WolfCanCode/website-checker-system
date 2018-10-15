package com.fpt.capstone.wcs.model;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrokenLink {

    @Id
    @GeneratedValue
    private Long id;
    private int httpCode;
    private String urlPage;
    private String urlLink;

}
