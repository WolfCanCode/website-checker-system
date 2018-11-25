package com.fpt.capstone.wcs.service.system.authenticate;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;

import java.util.List;

public interface AuthenticateService {
    public Website isAuthGetSingleSite(RequestCommonPOJO request);
    public WebsiteUserPOJO isAuthGetUserAndWebsite(RequestCommonPOJO request);
    public List<Website> isAuthGetListSite(RequestCommonPOJO request);
    public User isAuthGetSingleUser(RequestCommonPOJO request);
    public User isAuthAndManagerGet(ManagerRequestPOJO request);
}
