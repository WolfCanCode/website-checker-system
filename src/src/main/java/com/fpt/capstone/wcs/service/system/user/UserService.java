package com.fpt.capstone.wcs.service.system.user;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;

import java.util.Map;

public interface UserService {
    public Map<String, Object> isAuth( RequestCommonPOJO request);
    public Map<String, Object> doLogin(User user);

    }
