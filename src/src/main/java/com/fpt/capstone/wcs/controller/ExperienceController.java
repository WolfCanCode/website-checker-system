package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.SpeedTest;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.model.User;
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

    @PostMapping("/api/speedTest")
    public List<SpeedTest>  getDataSpeedTest(@RequestBody Url[] list){
        ExperienceService exp = new ExperienceService();
        List<SpeedTest> resultList = new ArrayList<>();
        for(int i = 0 ; i< list.length; i++) {
            SpeedTest item = exp.speedTestService(list[i].url);
            resultList.add(item);
        }

        return resultList;
    }
}
