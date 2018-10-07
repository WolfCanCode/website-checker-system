package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.SpeedTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpeedTestController {

    @GetMapping("/api/speedTest")
    public List<SpeedTest>  getDataSpeedTest(){
        List<SpeedTest> list = new ArrayList<>();
        list.add(new SpeedTest("10s", "20s", "3MB"));
        list.add(new SpeedTest("20s", "30s", "4MB"));
        list.add(new SpeedTest("30s", "40s", "5MB"));
        list.add(new SpeedTest("40s", "50s", "6MB"));

        return list;

    }
}
