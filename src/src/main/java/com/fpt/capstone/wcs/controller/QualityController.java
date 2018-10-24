package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.BrokenPageReport;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.repository.BrokenLinkRepository;
import com.fpt.capstone.wcs.repository.BrokenPageRepository;
import com.fpt.capstone.wcs.service.QualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QualityController {

    @Autowired
    BrokenPageRepository brokenPageRepository;
    @Autowired
    BrokenLinkRepository brokenLinkRepository;

    @PostMapping("/api/brokenLink")
    public List<BrokenLinkReport> getDataBrokenLink(@RequestBody Url[] list) throws InterruptedException {
        QualityService qlt = new QualityService();
        List<BrokenLinkReport> resultList = qlt.brokenLinkService(list);
        brokenLinkRepository.deleteAll();
        brokenLinkRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/brokenLink/lastest")
    public List<BrokenLinkReport> getLastestBrokenLink()
    {
        List<BrokenLinkReport> resultList = brokenLinkRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/brokenPage")
    public List<BrokenPageReport> getDataBrokenPage(@RequestBody Url[] list) throws InterruptedException {
        QualityService qlt = new QualityService();
        List<BrokenPageReport> resultList = qlt.brokenPageService(list);
        brokenPageRepository.deleteAll();
        brokenPageRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/brokenPage/lastest")
    public List<BrokenPageReport> getLastestBrokenPage()
    {
        List<BrokenPageReport> resultList = brokenPageRepository.findAll();
        return resultList;
    }
}
