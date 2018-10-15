package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.*;
import com.fpt.capstone.wcs.repository.BrokenLinkRepository;
import com.fpt.capstone.wcs.repository.BrokenPageRepository;
import com.fpt.capstone.wcs.repository.SpeedtestRepository;
import com.fpt.capstone.wcs.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ExperienceController {
    @Autowired
    SpeedtestRepository speedtestRepository;
    @Autowired
    BrokenPageRepository brokenPageRepository;
    @Autowired
    BrokenLinkRepository brokenLinkRepository;

    @PostMapping("/api/speedTest")
    public List<SpeedTest> getDataSpeedTest(@RequestBody Url[] list) throws InterruptedException {
        ExperienceService exp = new ExperienceService();
        List<SpeedTest> resultList = exp.speedTestService(list);
        speedtestRepository.deleteAll();
        speedtestRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/speedTest/lastest")
    public List<SpeedTest> getLastestSpeedTest()
    {
        List<SpeedTest> resultList = speedtestRepository.findAll();
        return resultList;
    }

    @GetMapping("/api/speedTest/isExist")
    public boolean checkAlreadyCheck(){
        boolean success = speedtestRepository.findAll().isEmpty();
        return  success;
    }


    @PostMapping("/api/brokenLink")
    public List<BrokenLink> getDataBrokenLink(@RequestBody Url[] list) throws InterruptedException {
        ExperienceService exp = new ExperienceService();
        List<BrokenLink> resultList = exp.brokenLinkService(list);
        brokenLinkRepository.deleteAll();
        brokenLinkRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/brokenLink/lastest")
    public List<BrokenLink> getLastestBrokenLink()
    {
        List<BrokenLink> resultList = brokenLinkRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/brokenPage")
    public List<BrokenPage> getDataBrokenPage(@RequestBody Url[] list) throws InterruptedException {
        ExperienceService exp = new ExperienceService();
        List<BrokenPage> resultList = exp.brokenPageService(list);
        brokenPageRepository.deleteAll();
        brokenPageRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/brokenPage/lastest")
    public List<BrokenPage> getLastestBrokenPage()
    {
            List<BrokenPage> resultList = brokenPageRepository.findAll();
        return resultList;
    }
}
