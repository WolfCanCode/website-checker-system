package com.fpt.capstone.wcs.controller.test;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import com.fpt.capstone.wcs.service.report.experience.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.*;

@RestController
public class ExperienceController {
    final
    ExperienceService experienceService;

    @Autowired
    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }


    @CrossOrigin
    @Transactional
    @PostMapping("/api/speedTest")
    public Map<String, Object> doSpeedTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return experienceService.doSpeedTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/speedTest/lastest")
    public Map<String, Object> getLastestSpeedTest(@RequestBody RequestCommonPOJO request) {
        return experienceService.getLastestSpeedTest(request);
    }

    @CrossOrigin
    @PostMapping("/api/speedTest/saveReport")
    public Map<String, Object> saveReport(@RequestBody RequestReportPOJO request) {
        return experienceService.saveReport(request);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/mobileLayoutTest")
    public Map<String, Object> getDataMobileLayout(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return experienceService.getDataMobileLayout(request);
    }

    @CrossOrigin
    @PostMapping("/api/mobileLayoutTest/lastest")
    public Map<String, Object> getLastestDataMobileLayout(@RequestBody RequestCommonPOJO request) {
        return experienceService.getLastestDataMobileLayout(request);
    }

    @CrossOrigin
    @PostMapping("/api/mobileLayoutTest/SaveReport")
    public Map<String, Object> saveMobileReport(@RequestBody RequestReportPOJO request) {
        return experienceService.saveMobileReport(request);
    }



}
