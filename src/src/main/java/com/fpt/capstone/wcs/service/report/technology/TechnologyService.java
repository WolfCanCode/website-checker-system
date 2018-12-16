package com.fpt.capstone.wcs.service.report.technology;

import com.fpt.capstone.wcs.model.entity.report.technology.ServerBehaviorReport;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TechnologyService {
    //Test Favicon
    public Map<String, Object> getfaviconTest(RequestCommonPOJO request);
    public Map<String, Object> getLastestFaviconTest(RequestCommonPOJO request);
    public Map<String, Object> saveFaviconTest(RequestReportPOJO request);
    //Test JS error
    public Map<String, Object> getJavaErrrorTest(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestJS(RequestCommonPOJO request);
    public Map<String, Object> saveJSTestReport(RequestReportPOJO request) ;
    //Test cookie
    public Map<String, Object> getCookies(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestCookies(RequestCommonPOJO request);
    public Map<String, Object> saveCookieReport(RequestReportPOJO request);
    //ServerBehavior
    public Map<String, Object> getServerBehavior(RequestCommonPOJO request)  throws IOException, InterruptedException;
    public Map<String, Object> saveServerBehaviorReport(RequestReportPOJO request);
    public Map<String, Object> getLastestServerBehavior(RequestCommonPOJO request);
}
