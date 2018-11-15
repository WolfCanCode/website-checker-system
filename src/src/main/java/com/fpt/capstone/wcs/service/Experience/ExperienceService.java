package com.fpt.capstone.wcs.service.Experience;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;

import java.util.Map;

public interface ExperienceService {
    public Map<String, Object> doSpeedTest(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestSpeedTest(RequestCommonPOJO request);

    public Map<String, Object> getDataMobileLayout(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestDataMobileLayout(RequestCommonPOJO request);

    }
