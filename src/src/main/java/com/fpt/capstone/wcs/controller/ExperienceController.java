package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.ExperienceService;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.*;

@RestController
public class ExperienceController {
    @Autowired
    SpeedtestRepository speedtestRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    PageOptionRepository pageOptionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebsiteRepository websiteRepository;
    @Autowired
    Authenticate authenticate;

    @Transactional
    @PostMapping("/api/speedTest")
    public Map<String, Object> getDataSpeedTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                ExperienceService exp = new ExperienceService();
                List<SpeedTestReport> resultList = exp.speedTestService(pages, pageOption);
                speedtestRepository.removeAllByPageOption(pageOption);
                speedtestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("speedtestReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                ExperienceService exp = new ExperienceService();
                List<SpeedTestReport> resultList = exp.speedTestService(pages, null);
                speedtestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("speedtestReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/speedTest/lastest")
    public Map<String, Object> getLastestSpeedTest(@RequestBody RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                List<SpeedTestReport> resultList = speedtestRepository.findAllByPageOption(pageOption);
                res.put("speedtestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<SpeedTestReport> resultList = speedtestRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("speedtestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @GetMapping("/api/speedTest/isExist")
    public boolean checkAlreadyCheck() {
        boolean success = speedtestRepository.findAll().isEmpty();
        return success;
    }


}
