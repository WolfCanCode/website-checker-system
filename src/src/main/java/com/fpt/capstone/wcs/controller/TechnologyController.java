package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.*;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.QualityService;
import com.fpt.capstone.wcs.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TechnologyController {
    @Autowired
    JSCheckRepository jsCheckRepository;
    ServerBehaviorRepository serverBehaviorRepository;
    @Autowired
    MissingFilesPagesRepository missingFilesPagesRepository;
    @Autowired
    CookieRepository cookieRepository;

    @PostMapping("/api/jsTest")
    public List<JSInfo> getDataPagesTest(@RequestBody Url[] list) throws InterruptedException {
        TechnologyService technologyService = new TechnologyService();
        List<JSInfo> resultList = technologyService.jsTestService(list);
        jsCheckRepository.deleteAll();
        jsCheckRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/jsTest/lastest")
    public List<JSInfo> getLastestSpeedTest() {
        List<JSInfo> resultList = jsCheckRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/svbehavior")
    public ServerBehavior getServerBehavior(@RequestBody Url url) throws InterruptedException, IOException {
        TechnologyService technologyService = new TechnologyService();
        ServerBehavior result = technologyService.checkServerBehavior(url);
//        serverBehaviorRepository.save(result);
        return result;
    }

    @PostMapping("/api/svbehavior/lastest")
    public ServerBehavior getLastestServerBehavior() {
        ServerBehavior result = serverBehaviorRepository.findAll().get(0);
        return result;
    }

    @PostMapping("/api/missingtest")
    public List<MissingFilesPages> getMissingFile(@RequestBody Url[] list){
        TechnologyService technologyService = new TechnologyService();
        String urlRoot="";
        for(int i =0; i< list.length;i++ ){
            Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(list[i].getUrl());
            while (matcher.find()){
                urlRoot = matcher.group();
            }
        }
        List<MissingFilesPages> result =  technologyService.getMissingFile(list, urlRoot);
        missingFilesPagesRepository.deleteAll();
        missingFilesPagesRepository.saveAll(result);
        System.out.println("Ket Thuc check");
        return result;
    }

    @PostMapping("/api/missingtest/lastest")
    public List<MissingFilesPages> getLastestMissingFile() {
        List<MissingFilesPages> result = missingFilesPagesRepository.findAll();
        return result;
    }



    @PostMapping("/api/cookie")
    public List<Cookie> getCookies(@RequestBody Url[] list) throws InterruptedException {
        TechnologyService technologyService = new TechnologyService();
        List<Cookie> resultList = technologyService.cookieService(list);
        cookieRepository.deleteAll();
        cookieRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/cookie/lastest")
    public List<Cookie> getLastestCookies()
    {
        List<Cookie> resultList = cookieRepository.findAll();
        return resultList;
    }

}
