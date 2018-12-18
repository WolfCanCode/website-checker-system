package com.fpt.capstone.wcs.model.entity.website;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Getter
@Setter
@Table(name = "enDict")
public class EnglishDictionary {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @JsonIgnore
    @Column(columnDefinition = "NVARCHAR(30)")
    private String word;

    @NotNull
    @JsonIgnore
    @Column(columnDefinition = "NVARCHAR(10)")
    private String type;

    @NotNull
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String definition;


}
