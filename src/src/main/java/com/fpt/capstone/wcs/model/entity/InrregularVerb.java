package com.fpt.capstone.wcs.model.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Data
@Entity
@Getter
@Setter
@Table(name = "InrregularVerb")
public class InrregularVerb {
    @Id
    @GeneratedValue
    private Long id;
    private String verbV1;
    private String verbV2;
    private String verbV3;


    public InrregularVerb(String verbV1, String verbV2, String verbV3) {
        this.verbV1 = verbV1;
        this.verbV2 = verbV2;
        this.verbV3 = verbV3;
    }
}
