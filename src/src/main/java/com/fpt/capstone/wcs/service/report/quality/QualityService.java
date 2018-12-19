package com.fpt.capstone.wcs.service.report.quality;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.report.quality.MissingFileReport;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;

import java.util.List;
import java.util.Map;

public interface QualityService {
    //Broken Link
    public Map<String, Object> getDataBrokenLink(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestBrokenLink(RequestCommonPOJO request);
    public Map<String, Object> saveBrokenLinkReport(RequestReportPOJO request);
    public Map<String, Object> getHistoryBrokenLinkList(RequestCommonPOJO request);
    public Map<String, Object> getHistoryBrokenLinkReport(RequestCommonPOJO request);
    //Guest
    public List<BrokenLinkReport> brokenLinkService(List<Page> list, PageOption option) throws InterruptedException;
    //Broken Page
    public Map<String, Object> getDataBrokenPage(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestBrokenPage(RequestCommonPOJO request);
    public Map<String, Object> saveBrokenPageReport(RequestReportPOJO request);
    public Map<String, Object> getHistoryBrokenPageTestReport(RequestCommonPOJO request);
    public Map<String, Object> getHistoryBrokenPageTestList(RequestCommonPOJO request);
    //Prohibited
    public Map<String, Object> getDataProhibitedContent(RequestCommonPOJO request) throws InterruptedException;
    public Map<String, Object> getLastestProhibitedContent(RequestCommonPOJO request);
    public Map<String, Object> saveProhibitedContentReport(RequestReportPOJO request);
    public Map<String, Object> getHistoryProhibitedContentReport(RequestCommonPOJO request);
    public Map<String, Object> getHistoryProhibitedContentList(RequestCommonPOJO request);
    //Missing File
    public Map<String, Object> getMissingFile(MissingFilePOJO request) throws InterruptedException;
    public Map<String, Object> getLastestMissingFile(MissingFilePOJO request);
    public Map<String, Object> saveMissingFileReport(RequestReportPOJO request);
    public Map<String, Object> getHistoryMissingFileTestReport(RequestCommonPOJO request);
    public Map<String, Object> getHistoryMissingFileTestList(RequestCommonPOJO request);
    //Guest
    public List<MissingFileReport> getMissingFile(List<Page> list, PageOption option, String urlNew) throws InterruptedException;
}
