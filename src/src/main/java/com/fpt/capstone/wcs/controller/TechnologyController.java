package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.*;
import com.fpt.capstone.wcs.model.entity.CookieReport;
import com.fpt.capstone.wcs.model.entity.JavascriptReport;
import com.fpt.capstone.wcs.model.entity.MissingFileReport;
import com.fpt.capstone.wcs.model.entity.ServerBehaviorReport;
import com.fpt.capstone.wcs.repository.*;
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
    public List<JavascriptReport> getDataPagesTest(@RequestBody Url[] list) throws InterruptedException {
        TechnologyService technologyService = new TechnologyService();
        List<JavascriptReport> resultList = technologyService.jsTestService(list);
        jsCheckRepository.deleteAll();
        jsCheckRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/jsTest/lastest")
    public List<JavascriptReport> getLastestSpeedTest() {
        List<JavascriptReport> resultList = jsCheckRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/svbehavior")
    public ServerBehaviorReport getServerBehavior(@RequestBody Url url) throws InterruptedException, IOException {
        TechnologyService technologyService = new TechnologyService();
        ServerBehaviorReport result = technologyService.checkServerBehavior(url);
//        serverBehaviorRepository.save(result);
        return result;
    }

    @PostMapping("/api/svbehavior/lastest")
    public ServerBehaviorReport getLastestServerBehavior() {
        ServerBehaviorReport result = serverBehaviorRepository.findAll().get(0);
        return result;
    }

    @PostMapping("/api/missingtest")
    public List<MissingFileReport> getMissingFile(@RequestBody Url[] list){
        TechnologyService technologyService = new TechnologyService();
        String urlRoot="";
        for(int i =0; i< list.length;i++ ){
            Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(list[i].getUrl());
            while (matcher.find()){
                urlRoot = matcher.group();
            }
        }
        List<MissingFileReport> result =  technologyService.getMissingFile(list, urlRoot);
        missingFilesPagesRepository.deleteAll();
        missingFilesPagesRepository.saveAll(result);
        System.out.println("Ket Thuc check");
        return result;
    }

    @PostMapping("/api/missingtest/lastest")
    public List<MissingFileReport> getLastestMissingFile() {
        List<MissingFileReport> result = missingFilesPagesRepository.findAll();
        return result;
    }



    @PostMapping("/api/cookie")
    public List<CookieReport> getCookies(@RequestBody Url[] list) throws InterruptedException {
        TechnologyService technologyService = new TechnologyService();
        List<CookieReport> resultList = technologyService.cookieService(list);
        cookieRepository.deleteAll();
        cookieRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/cookie/lastest")
    public List<CookieReport> getLastestCookies()
    {
        List<CookieReport> resultList = cookieRepository.findAll();
        return resultList;
    }

}
