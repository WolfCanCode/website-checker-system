package com.fpt.capstone.wcs.service.Technology;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface TechnologyService {
    public Map<String, Object> faviconTest(@RequestBody RequestCommonPOJO request);
    public Map<String, Object> getLastestFaviconTest(@RequestBody RequestCommonPOJO request);

    public Map<String, Object> getJavaErrrorTest(@RequestBody RequestCommonPOJO request);
    public Map<String, Object> getLastestSpeedTest(@RequestBody RequestCommonPOJO request);

    public Map<String, Object> getCookies(@RequestBody RequestCommonPOJO request);
    public Map<String, Object> getLastestCookies(@RequestBody RequestCommonPOJO request);
}
