package com.fpt.capstone.wcs.service.Quality;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface QualityService {
    //Broken Link
    public Map<String, Object> getDataBrokenLink(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestBrokenLink(RequestCommonPOJO request);
    //Broken Page
    public Map<String, Object> getDataBrokenPage(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestBrokenPage(RequestCommonPOJO request);
    //Prohibited
    public Map<String, Object> getDataProhibitedContent(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestProhibitedContent(RequestCommonPOJO request);

    public Map<String, Object> getMissingFile(@RequestBody MissingFilePOJO request);
    public Map<String, Object> getLastestMissingFile(@RequestBody MissingFilePOJO request);
}
