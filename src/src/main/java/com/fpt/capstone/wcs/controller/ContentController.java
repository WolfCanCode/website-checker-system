package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.LinkRedirection;
import com.fpt.capstone.wcs.model.Pages;
import com.fpt.capstone.wcs.model.SpeedTest;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.repository.LinkRedirectionRepository;
import com.fpt.capstone.wcs.repository.PageTestRepository;
import com.fpt.capstone.wcs.service.ContentService;
import com.fpt.capstone.wcs.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ContentController {
//    @Autowired
//    ContentService contentService;
    @Autowired
    PageTestRepository pageTestRepository;
    @Autowired
    LinkRedirectionRepository linkRedirectionRepository;



    @PostMapping("/api/pagestest")
    public List<Pages> getDataPagesTest(@RequestBody Url[] list) throws IOException {
        ContentService contentService = new ContentService();
        List<Pages> resultList = contentService.getPageInfor(list);
       pageTestRepository.deleteAll();
       pageTestRepository.saveAll(resultList);
        return resultList;
    }
    @PostMapping("/api/pagestest/lastest")
    public List<Pages> getLastestSpeedTest()
    {
        List<Pages> resultList =  pageTestRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/redirectiontest")
    public List<LinkRedirection> getDataRedirectTest(@RequestBody Url[] list) throws IOException {
        ContentService con  = new ContentService();
        List<LinkRedirection> resultList = con.redirectionTest(list);
       linkRedirectionRepository.deleteAll();
       linkRedirectionRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/redirectiontest/lastest")
    public List<LinkRedirection> getLastestLinkRedirection()
    {
        List<LinkRedirection> resultList =  linkRedirectionRepository.findAll();
        return resultList;
    }
}
