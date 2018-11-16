package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.TechnologyService;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    JSCheckRepository jsCheckRepository;
    ServerBehaviorRepository serverBehaviorRepository;
//    @Autowired
//    MissingFilesPagesRepository missingFilesPagesRepository;
    @Autowired
    CookieRepository cookieRepository;


    @Autowired
    FaviconRepository faviconRepository;
    @Autowired
    TechnologyService technologyService;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    PageOptionRepository pageOptionRepository;
    @Autowired
    Authenticate authenticate;

    @PostMapping("/api/jsTest")
    public List<JavascriptReport> getDataPagesTest(@RequestBody UrlPOJO[] list) throws InterruptedException {
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
    public ServerBehaviorReport getServerBehavior(@RequestBody UrlPOJO url) throws InterruptedException, IOException {
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

//





    @Transactional
    @PostMapping("/api/cookie")
    public Map<String, Object> getCookies(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<CookieReport> resultList = technologyService.cookieService(pages, pageOption);
                cookieRepository.removeAllByPageOption(pageOption);
                cookieRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("cookieReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                TechnologyService technologyService = new TechnologyService();
                List<CookieReport> resultList = technologyService.cookieService(pages, null);
                cookieRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("cookieReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/cookie/lastest")
    public Map<String, Object> getLastestCookies(@RequestBody RequestCommonPOJO request)
    {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }

            if(request.getPageOptionId()!=-1) {

                List<CookieReport> resultList = cookieRepository.findAllByPageOption(pageOption);
                res.put("cookieReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<CookieReport> resultList = cookieRepository.findAllByPageOption(null);
                res.put("cookieReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

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

    @PostMapping("/api/favicontest")
    public Map<String, Object> getDataPagesTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                String urlRoot="";
        for(int i =0; i< pages.size();i++ ){
            Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(pages.get(i).getUrl());
            while (matcher.find()){
                urlRoot = matcher.group();
            }
        }
                TechnologyService exp = new TechnologyService();
                List<FaviconReport> resultList = exp.checkFavicon(pages, pageOption, urlRoot);
                faviconRepository.removeAllByPageOption(pageOption);
                faviconRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("faviconReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                String urlRoot="";
                for(int i =0; i< pages.size();i++ ){
                    Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(pages.get(i).getUrl());
                    while (matcher.find()){
                        urlRoot = matcher.group();
                    }
                }
                TechnologyService exp = new TechnologyService();
                List<FaviconReport> resultList = exp.checkFavicon(pages, null,urlRoot);
                faviconRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("faviconReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/favicontest/lastest")
    public Map<String, Object> getLastestPageTest(@RequestBody RequestCommonPOJO request)
    {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                List<FaviconReport> resultList = faviconRepository.findAllByPageOption(pageOption);
                res.put("faviconReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<FaviconReport> resultList = faviconRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("faviconReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }
}
