package com.fpt.capstone.wcs.service.Header;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;

import java.util.Map;

public interface HeaderService {
    public Map<String, Object> headerStaff(RequestCommonPOJO request);
    public Map<String, Object> headerManager(RequestCommonPOJO request);

}
