package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.ExperienceService;
import com.fpt.capstone.wcs.service.QualityService;

import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class QualityController {

    @Autowired
    BrokenPageRepository brokenPageRepository;
    @Autowired
    BrokenLinkRepository brokenLinkRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    PageOptionRepository pageOptionRepository;

    @Autowired
    MissingFilesPagesRepository missingFilesPagesRepository;

    @Autowired
    Authenticate authenticate;
    @Transactional
    @PostMapping("/api/brokenLink")
    public Map<String, Object> getDataBrokenLink(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                QualityService qlt = new QualityService();
                List<BrokenLinkReport> resultList = qlt.brokenLinkService(pages, pageOption);
                brokenLinkRepository.removeAllByPageOption(pageOption);
                brokenLinkRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenLinkReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                QualityService qlt = new QualityService();
                List<BrokenLinkReport> resultList = qlt.brokenLinkService(pages, null);
                brokenLinkRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenLinkReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }



    }

    @PostMapping("/api/brokenLink/lastest")
    public Map<String, Object> getLastestBrokenLink(@RequestBody RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }

            if(request.getPageOptionId()!=-1) {

                List<BrokenLinkReport> resultList = brokenLinkRepository.findAllByPageOption(pageOption);
                res.put("brokenLinkReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<BrokenLinkReport> resultList = brokenLinkRepository.findAllByPageOption(null);
                res.put("brokenLinkReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @GetMapping("/api/brokenLink/isExist")
    public boolean checkAlreadyBrokenLinkCheck() {
        boolean success = brokenLinkRepository.findAll().isEmpty();
        return success;
    }
    @Transactional
    @PostMapping("/api/brokenPage")
    public Map<String, Object> getDataBrokenPage(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                QualityService qlt = new QualityService();
                List<BrokenPageReport> resultList = qlt.brokenPageService(pages, pageOption);
                brokenPageRepository.removeAllByPageOption(pageOption);
                brokenPageRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenPageReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                QualityService qlt = new QualityService();
                List<BrokenPageReport> resultList = qlt.brokenPageService(pages, null);
                brokenPageRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenPageReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/brokenPage/lastest")
    public Map<String, Object> getLastestBrokenPage(@RequestBody RequestCommonPOJO request)
    {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }

            if(request.getPageOptionId()!=-1) {

                List<BrokenPageReport> resultList = brokenPageRepository.findAllByPageOption(pageOption);
                res.put("brokenPageReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<BrokenPageReport> resultList = brokenPageRepository.findAllByPageOption(null);
                res.put("brokenPageReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @GetMapping("/api/brokenPage/isExist")
    public boolean checkAlreadyBrokenPageCheck() {
        boolean success = brokenPageRepository.findAll().isEmpty();
        return success;
    }

    @PostMapping("/api/missingtest")
    public List<MissingFileReport> getMissingFile(@RequestBody MissingFilePOJO[] request) throws InterruptedException {
        QualityService qualityService = new QualityService();
        List<MissingFileReport> result= new ArrayList<>();
        System.out.println( request.length);
        UrlPOJO[] list = new UrlPOJO[2];
        list[0]= new UrlPOJO("https://www.bhcosmetics.com/");
        list[1]= new UrlPOJO("https://thanhnien.vn/");
        String urlRoot="";
        for(int i =0; i< list.length;i++ ){
            Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(list[i].getUrl());
            while (matcher.find()){
                urlRoot = matcher.group();
            }
        }
        if(request.length==0|| request.length==4){
            System.out.println("Vo ne");
            result =  qualityService.getMissingFile(list, urlRoot);
        }
        else{
            for (MissingFilePOJO missingFilePOJODTO :request){
               int typeSwicth = missingFilePOJODTO.getType();
                System.out.println(typeSwicth);
                switch (missingFilePOJODTO.getType()){
                    case 1:{
                        result.addAll(qualityService.getMissingFileImg(list, urlRoot));
                        break;
                    }
                    case 2:{
                        result.addAll(qualityService.getMissingFileCss(list,urlRoot));
                        break;
                    }
                    case 3:{
                        result.addAll(qualityService.getMissingFileDoc(list, urlRoot));
                        break;

                    }
                    case 4:{
                        result.addAll(qualityService.getMissingFileARCHIVES(list, urlRoot));
                        break;
                    }
                }
            }
        }


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
}
