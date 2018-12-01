package com.fpt.capstone.wcs.service.report.content;

import com.fpt.capstone.wcs.model.entity.report.content.RedirectionReport;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;

import java.util.List;
import java.util.Map;

public interface ContentService {
    //Page Test
    public Map<String, Object> getDataPagesTest(RequestCommonPOJO request);
    public Map<String, Object> getLastestPageTest(RequestCommonPOJO request);
    public Map<String, Object> savePageReport(RequestReportPOJO request);
    //Redirection Test
    public Map<String, Object> getDataRedirectTest(RequestCommonPOJO request);
    public Map<String, Object> getLastestLinkRedirection(RequestCommonPOJO request);
    public Map<String, Object> saveRedirectionReport(RequestReportPOJO request);
    public List<RedirectionReport> redirectionTest(List<Page> list, PageOption option); //export reuse
        //Contact Detail
    public Map<String, Object> getDataContactDetail(RequestCommonPOJO request);
    public Map<String, Object> getLastestContactDetail(RequestCommonPOJO request);
    public Map<String, Object> saveContactDetailReport(RequestReportPOJO request);
}
