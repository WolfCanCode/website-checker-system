package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.BrokenLink;
import com.fpt.capstone.wcs.model.BrokenPage;
import com.fpt.capstone.wcs.model.LinkRedirection;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.repository.BrokenLinkRepository;
import com.fpt.capstone.wcs.repository.BrokenPageRepository;
import com.fpt.capstone.wcs.repository.PageTestRepository;
import com.fpt.capstone.wcs.service.ContentService;
import com.fpt.capstone.wcs.service.ExperienceService;
import com.fpt.capstone.wcs.service.QualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class QualityController {

    @Autowired
    BrokenPageRepository brokenPageRepository;
    @Autowired
    BrokenLinkRepository brokenLinkRepository;

    @PostMapping("/api/brokenLink")
    public List<BrokenLink> getDataBrokenLink(@RequestBody Url[] list) throws InterruptedException {
        QualityService qlt = new QualityService();
        List<BrokenLink> resultList = qlt.brokenLinkService(list);
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
        QualityService qlt = new QualityService();
        List<BrokenPage> resultList = qlt.brokenPageService(list);
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
