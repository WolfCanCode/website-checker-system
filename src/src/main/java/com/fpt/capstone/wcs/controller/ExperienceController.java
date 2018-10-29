package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.*;
import com.fpt.capstone.wcs.model.entity.SpeedTestReport;
import com.fpt.capstone.wcs.repository.SpeedtestRepository;
import com.fpt.capstone.wcs.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExperienceController {
    @Autowired
    SpeedtestRepository speedtestRepository;

    @PostMapping("/api/speedTest")
    public List<SpeedTestReport> getDataSpeedTest(@RequestBody Url[] list) throws InterruptedException {
        ExperienceService exp = new ExperienceService();
        List<SpeedTestReport> resultList = exp.speedTestService(list);
        speedtestRepository.deleteAll();
        speedtestRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/speedTest/lastest")
    public List<SpeedTestReport> getLastestSpeedTest()
    {
        List<SpeedTestReport> resultList = speedtestRepository.findAll();
        return resultList;
    }

    @GetMapping("/api/speedTest/isExist")
    public boolean checkAlreadyCheck(){
        boolean success = speedtestRepository.findAll().isEmpty();
        return  success;
    }



}
