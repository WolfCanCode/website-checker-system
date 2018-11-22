package com.fpt.capstone.wcs.model.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@Table(name = "Topic")
public class Topic {
    @Id
    @GeneratedValue
    private Long id;
    private String typeName;
    private String description;
}
