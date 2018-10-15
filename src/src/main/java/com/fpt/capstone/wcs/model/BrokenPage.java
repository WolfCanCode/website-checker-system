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
public class BrokenPage {
    @Id
    @GeneratedValue
    private Long id;
    private String urlPage;
    private String stt;
    private int httpCode;

}
