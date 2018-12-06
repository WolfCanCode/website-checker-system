package com.fpt.capstone.wcs.service.report.technology;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface TechnologyService {
    //Test Favicon
    public Map<String, Object> getfaviconTest(RequestCommonPOJO request);
    public Map<String, Object> getLastestFaviconTest(RequestCommonPOJO request);
    //Test JS error
    public Map<String, Object> getJavaErrrorTest(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestJS(RequestCommonPOJO request);
    public Map<String, Object> saveJSTestReport(RequestReportPOJO request) ;
    //Test cookie
    public Map<String, Object> getCookies(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestCookies(RequestCommonPOJO request);
    public Map<String, Object> saveCookieReport(RequestReportPOJO request);
}
