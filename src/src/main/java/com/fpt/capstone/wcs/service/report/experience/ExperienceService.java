package com.fpt.capstone.wcs.service.report.experience;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;

import java.util.Map;

public interface ExperienceService {
    //Speed test
    public Map<String, Object> doSpeedTest(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestSpeedTest(RequestCommonPOJO request);
    public Map<String, Object> saveSpeedTestReport(RequestReportPOJO request);
    public Map<String, Object> getHistorySpeedTestReport(RequestCommonPOJO request);
    public Map<String, Object> getHistorySpeedTestList(RequestCommonPOJO request);
    //Mobile layout
    public Map<String, Object> getDataMobileLayout(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestDataMobileLayout(RequestCommonPOJO request);
    public Map<String, Object> saveMobileReport(RequestReportPOJO request);
    public Map<String, Object> getHistoryMobileLayoutTestReport(RequestCommonPOJO request);
    public Map<String, Object> getHistoryMobileLayoutTestList(RequestCommonPOJO request);


}
