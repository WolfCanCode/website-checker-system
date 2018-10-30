package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.ExperienceService;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExperienceController {
    @Autowired
    SpeedtestRepository speedtestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebsiteRepository websiteRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;

    @PostMapping("/api/speedTest")
    public Map<String, Object> getDataSpeedTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        User user = userRepository.findOneByIdAndToken(request.getUserId(), request.getUserToken());
        if (user != null) {
            Website website = websiteRepository.findOneByUserAndId(user, request.getWebsiteId());
            if (website != null) {
                Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
                List<Page> pages = pageRepository.findAllByWebsiteAndVersion(website, version);
                ExperienceService exp = new ExperienceService();
                List<SpeedTestReport> resultList = exp.speedTestService(pages);
                speedtestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("speedtestReport", resultList);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
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
