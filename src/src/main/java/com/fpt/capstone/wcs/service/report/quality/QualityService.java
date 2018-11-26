package com.fpt.capstone.wcs.service.report.quality;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;

import java.util.Map;

public interface QualityService {
    //Broken Link
    public Map<String, Object> getDataBrokenLink(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestBrokenLink(RequestCommonPOJO request);
    public Map<String, Object> saveBrokenLinkReport(RequestReportPOJO request);
    //Broken Page
    public Map<String, Object> getDataBrokenPage(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestBrokenPage(RequestCommonPOJO request);
    public Map<String, Object> saveBrokenPageReport(RequestReportPOJO request);
    //Prohibited
    public Map<String, Object> getDataProhibitedContent(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestProhibitedContent(RequestCommonPOJO request);
    public Map<String, Object> saveProhibitedContentReport(RequestReportPOJO request);
    //Missing File
    public Map<String, Object> getMissingFile(MissingFilePOJO request);
    public Map<String, Object> getLastestMissingFile(MissingFilePOJO request);
}
