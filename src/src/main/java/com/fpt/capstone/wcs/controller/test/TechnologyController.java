package com.fpt.capstone.wcs.controller.test;

import com.fpt.capstone.wcs.model.entity.report.technology.ServerBehaviorReport;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;

import com.fpt.capstone.wcs.repository.report.technology.CookieRepository;
import com.fpt.capstone.wcs.repository.report.technology.ServerBehaviorRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.repository.website.PageRepository;
import com.fpt.capstone.wcs.repository.website.VersionRepository;
import com.fpt.capstone.wcs.service.report.technology.TechnologyService;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;

@RestController
public class TechnologyController {

    @Autowired
    TechnologyService technologyService;

    @CrossOrigin
    @PostMapping("/api/jsTest")
    public Map<String, Object> getJavaErrrorTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
         return  technologyService.getJavaErrrorTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/jsTest/lastest")
    public Map<String, Object> getLastestSpeedTest(@RequestBody RequestCommonPOJO request) {
       return technologyService.getLastestJS(request);
    }

    @CrossOrigin
    @PostMapping("/api/jsTest/getHistoryList")
    public Map<String, Object> getHistoryJSTestList(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryJSTestList(request);
    }

    @CrossOrigin
    @PostMapping("/api/jsTest/getHistoryReport")
    public Map<String, Object> getHistoryJSTestReport(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryJSTestReport(request);
    }


    @CrossOrigin
    @PostMapping("/api/jsTest/saveReport")
    public Map<String, Object> saveJSTestReport(@RequestBody RequestReportPOJO request) {
        return technologyService.saveJSTestReport(request);
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior")
    public Map<String, Object> getServerBehavior(@RequestBody RequestCommonPOJO request) throws InterruptedException, IOException {
        return  technologyService.getServerBehavior(request);
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior/lastest")
    public Map<String, Object> getLastestServerBehavior(@RequestBody RequestCommonPOJO request) {
        return technologyService.getLastestServerBehavior(request);
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior/getHistoryList")
    public Map<String, Object> getHistoryServerBehaviorTestList(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryServerBehaviorTestList(request);
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior/getHistoryReport")
    public Map<String, Object> getHistoryServerBehaviorTestReport(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryServerBehaviorTestReport(request);
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior/saveReport")
    public Map<String, Object> saveServerBehaviorReport(@RequestBody RequestReportPOJO request) {
        return technologyService.saveServerBehaviorReport(request);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/cookie")
    public Map<String, Object> getCookies(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return  technologyService.getCookies(request);
    }

    @CrossOrigin
    @PostMapping("/api/cookie/lastest")
    public Map<String, Object> getLastestCookies(@RequestBody RequestCommonPOJO request) {
        return  technologyService.getLastestCookies(request);
    }

    @CrossOrigin
    @PostMapping("/api/cookie/getHistoryList")
    public Map<String, Object> getHistoryCookieTestList(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryCookiesTestList(request);
    }

    @CrossOrigin
    @PostMapping("/api/cookie/getHistoryReport")
    public Map<String, Object> getHistoryCookiesTestReport(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryCookiesTestReport(request);
    }

    @CrossOrigin
    @PostMapping("/api/cookie/SaveReport")
    public Map<String, Object> saveCookiesReport(@RequestBody RequestReportPOJO request) {
        return technologyService.saveCookieReport(request);
    }

    @Transactional
    @PostMapping("/api/faviconTest")
    public  Map<String, Object> getDataFaviconTest(@RequestBody  RequestCommonPOJO request) throws InterruptedException {
        return  technologyService.getfaviconTest(request);
    }


    @PostMapping("/api/faviconTest/lastest")
    public Map<String, Object> getLastestPageTest(@RequestBody RequestCommonPOJO request)
    {
       return technologyService.getLastestFaviconTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/faviconTest/getHistoryList")
    public Map<String, Object> getHistoryFaviconTestList(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryFaviconTestList(request);
    }

    @CrossOrigin
    @PostMapping("/api/faviconTest/getHistoryReport")
    public Map<String, Object> getHistoryFaviconTestReport(@RequestBody RequestCommonPOJO request) {
        return technologyService.getHistoryFaviconTestReport(request);
    }

    @PostMapping("/api/faviconTest/saveReport")
    public Map<String, Object> saveFaviconReport(@RequestBody RequestReportPOJO report){
        return technologyService.saveFaviconTest(report);
    }
}
