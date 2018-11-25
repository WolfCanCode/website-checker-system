package com.fpt.capstone.wcs.model.pojo;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WebsiteUserPOJO {
    private User user;
    private Website website;
}
