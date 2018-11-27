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
       return technologyService.getLastestSpeedTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior")
    public ServerBehaviorReport getServerBehavior(@RequestBody UrlPOJO url) throws InterruptedException, IOException {
//        com.fpt.capstone.wcs.service.TechnologyService technologyService = new com.fpt.capstone.wcs.service.TechnologyService();
//        ServerBehaviorReport result = technologyService.checkServerBehavior(url);
//        serverBehaviorRepository.save(result);
        return null;
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior/lastest")
    public ServerBehaviorReport getLastestServerBehavior() {
//        ServerBehaviorReport result = serverBehaviorRepository.findAll().get(0);
        return null;
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
}