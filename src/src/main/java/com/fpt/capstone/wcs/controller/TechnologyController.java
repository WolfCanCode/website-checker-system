package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.TechnologyService;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TechnologyController {
//    @Autowired
//    JSCheckRepository jsCheckRepository;
    @Autowired
    ServerBehaviorRepository serverBehaviorRepository;
//    @Autowired
//    MissingFilesPagesRepository missingFilesPagesRepository;
    @Autowired
    CookieRepository cookieRepository;

//
//    @Autowired
//    FaviconRepository faviconRepository;
//    @Autowired
//    TechnologyService technologyService;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    PageOptionRepository pageOptionRepository;
    @Autowired
    Authenticate authenticate;


    @Autowired
    com.fpt.capstone.wcs.service.Technology.TechnologyService technologyServiceS1;

    @CrossOrigin
    @PostMapping("/api/jsTest")
    public Map<String, Object> getJavaErrrorTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
//        TechnologyService technologyService = new TechnologyService();
       // List<JavascriptReport> resultList = technologyService.jsTestService(list);
//        jsCheckRepository.deleteAll();
//        jsCheckRepository.saveAll(resultList);
         return  technologyServiceS1.getJavaErrrorTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/jsTest/lastest")
    public Map<String, Object> getLastestSpeedTest(@RequestBody RequestCommonPOJO request) {
//
//        List<JavascriptReport> resultList = jsCheckRepository.findAll();
//        return resultList;
       return technologyServiceS1.getLastestSpeedTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior")
    public ServerBehaviorReport getServerBehavior(@RequestBody UrlPOJO url) throws InterruptedException, IOException {
        TechnologyService technologyService = new TechnologyService();
        ServerBehaviorReport result = technologyService.checkServerBehavior(url);
//        serverBehaviorRepository.save(result);
        return result;
    }

    @CrossOrigin
    @PostMapping("/api/svbehavior/lastest")
    public ServerBehaviorReport getLastestServerBehavior() {
        ServerBehaviorReport result = serverBehaviorRepository.findAll().get(0);
        return result;
    }

//





    @CrossOrigin
    @Transactional
    @PostMapping("/api/cookie")
    public Map<String, Object> getCookies(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return  technologyServiceS1.getCookies(request);
    }

    @CrossOrigin
    @PostMapping("/api/cookie/lastest")
    public Map<String, Object> getLastestCookies(@RequestBody RequestCommonPOJO request) {
        return  technologyServiceS1.getLastestCookies(request);
    }

    @CrossOrigin
    @GetMapping("/api/cookie/isExist")
    public boolean checkAlreadyCookieCheck() {
        boolean success = cookieRepository.findAll().isEmpty();
        return success;
    }

//    @PostMapping("/api/favicontest")
//    public List<FaviconReport> getDataFaviconTest(@RequestBody UrlPOJO[] list) throws InterruptedException {
//        TechnologyService technologyService = new TechnologyService();
//        String urlRoot="";
//        for(int i =0; i< list.length;i++ ){
//            Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
//            Matcher matcher = pattern.matcher(list[i].getUrl());
//            while (matcher.find()){
//                urlRoot = matcher.group();
//            }
//        }
//        List<FaviconReport> resultList = technologyService.checkFavicon(list,urlRoot);
//        faviconRepository.deleteAll();
//        faviconRepository.saveAll(resultList);
//        return resultList;
//    }

    @CrossOrigin
    @PostMapping("/api/favicontest")
    public Map<String, Object> getDataPagesTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return technologyServiceS1.faviconTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/favicontest/lastest")
    public Map<String, Object> getLastestPageTest(@RequestBody RequestCommonPOJO request)
    {
       return technologyServiceS1.getLastestFaviconTest(request);
    }
}
