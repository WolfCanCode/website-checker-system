package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.service.Experience.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.*;

@RestController
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;

    @Transactional
    @PostMapping("/api/speedTest")
    public Map<String, Object> doSpeedTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return experienceService.doSpeedTest(request);
    }

    @PostMapping("/api/speedTest/lastest")
    public Map<String, Object> getLastestSpeedTest(@RequestBody RequestCommonPOJO request) {
        return experienceService.getLastestSpeedTest(request);
    }

}
