package com.fpt.capstone.wcs.service.Content;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ContentService {

    public Map<String, Object> getDataPagesTest(@RequestBody RequestCommonPOJO request);
    public Map<String, Object> getLastestPageTest(@RequestBody RequestCommonPOJO request);

    public Map<String, Object> getDataRedirectTest(@RequestBody RequestCommonPOJO request);
    public Map<String, Object> getLastestLinkRedirection(@RequestBody RequestCommonPOJO request);

    public Map<String, Object> getDataContactDetail(@RequestBody RequestCommonPOJO request);
    public Map<String, Object> getLastestContactDetail(@RequestBody RequestCommonPOJO request);
}
