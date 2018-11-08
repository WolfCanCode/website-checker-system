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

    @PostMapping("/api/speedTest")
    public Map<String, Object> getDataSpeedTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        System.out.println(website.getUrl());
            if (website != null) {
                Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
                PageOption pageOption = pageOptionRepository.findFirstByWebsiteOrderByTimeDesc(website);
                if (pageOption == null) {
                    PageOption tmp = new PageOption();
                    tmp.setOptionNumber(1);
                   tmp.setTime(new Date());
                   tmp.setWebsite(website);
                   pageOptionRepository.save(tmp);
                   pageOption = pageOptionRepository.findFirstByWebsite(website);
                   List<Page> pages = pageRepository.findAllByWebsiteAndVersionAndTypeEquals(website, version,1);
                    for (int i = 0; i < pages.size(); i++) {
                       Page pageTmp = pages.get(i);
                       List<PageOption> listOption = new ArrayList<>();
                       listOption.add(pageOption);
                       pageTmp.setPageOptions(listOption);
                       pages.set(i, pageTmp);
                    }
                   ExperienceService exp = new ExperienceService();
                 List<SpeedTestReport> resultList = exp.speedTestService(pages);
                    speedtestRepository.saveAll(resultList);
                    res.put("action", Constant.SUCCESS);
                    res.put("speedtestReport", resultList);
                   res.put("action", Constant.SUCCESS);
                    res.put("speedtestReport", resultList);
                   return res;
               } else {
                   List<Page> pages = pageRepository.findAllByWebsiteAndVersionAndPageOptions(website, version, pageOption);
                    ExperienceService exp = new ExperienceService();
                    List<SpeedTestReport> resultList = exp.speedTestService(pages);
                   speedtestRepository.saveAll(resultList);
                    res.put("action", Constant.SUCCESS);
                    res.put("test", website);
                    res.put("speedtestReport", resultList);
                    return res;
               }
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
           }
    }

    @PostMapping("/api/speedTest/lastest")
    public List<SpeedTestReport> getLastestSpeedTest() {
        List<SpeedTestReport> resultList = speedtestRepository.findAll();
        return resultList;
    }

    @GetMapping("/api/speedTest/isExist")
    public boolean checkAlreadyCheck() {
        boolean success = speedtestRepository.findAll().isEmpty();
        return success;
    }


}
