package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.JSInfo;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.repository.JSCheckRepository;
import com.fpt.capstone.wcs.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TechnologyController {
    @Autowired
    JSCheckRepository jsCheckRepository;

    @PostMapping("/api/jsTest")
    public List<JSInfo> getDataPagesTest(@RequestBody Url[] list) throws InterruptedException {
        TechnologyService technologyService = new TechnologyService();
        List<JSInfo> resultList = technologyService.jsTestService(list);
        jsCheckRepository.deleteAll();
        jsCheckRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/jsTest/lastest")
    public List<JSInfo> getLastestSpeedTest() {
        List<JSInfo> resultList = jsCheckRepository.findAll();
        return resultList;
    }
}
