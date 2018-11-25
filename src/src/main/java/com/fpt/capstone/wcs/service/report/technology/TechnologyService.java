package com.fpt.capstone.wcs.service.report.technology;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface TechnologyService {
    //Test Favicon
    public Map<String, Object> getfaviconTest(@RequestBody RequestCommonPOJO request);
    public Map<String, Object> getLastestFaviconTest(@RequestBody RequestCommonPOJO request);
    //Test JS error
    public Map<String, Object> getJavaErrrorTest(@RequestBody RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestSpeedTest(@RequestBody RequestCommonPOJO request);
    //Test cookie
    public Map<String, Object> getCookies(@RequestBody RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestCookies(@RequestBody RequestCommonPOJO request);
}
