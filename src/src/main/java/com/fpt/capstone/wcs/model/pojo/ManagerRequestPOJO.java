package com.fpt.capstone.wcs.model.pojo;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.website.WarningWord;
import com.fpt.capstone.wcs.model.entity.user.Website;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Getter
@Setter
public class ManagerRequestPOJO {
    private long managerId;
    private String managerToken;
    private Website website;
    private User staff;
    private WarningWord warningWord;
    private Long[] listStaffId;
    private long reportDate;
    private String reportType;
    private long pageOptionId;
}
