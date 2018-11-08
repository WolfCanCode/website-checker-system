package com.fpt.capstone.wcs.model.pojo;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class ManagerRequestPOJO {
    private long managerId;
    private String managerToken;
    private Website website;
    private User staff;
    private Long[] listStaffId;
}
