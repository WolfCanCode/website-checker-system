package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.*;
import com.fpt.capstone.wcs.model.entity.ContactReport;
import com.fpt.capstone.wcs.model.entity.RedirectionReport;
import com.fpt.capstone.wcs.model.entity.Pages;
import com.fpt.capstone.wcs.repository.ContactDetailRepository;
import com.fpt.capstone.wcs.repository.LinkRedirectionRepository;
import com.fpt.capstone.wcs.repository.PageTestRepository;
import com.fpt.capstone.wcs.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    @Autowired
    ContactDetailRepository contactDetailRepository;



    @PostMapping("/api/pagestest")
    public List<Pages> getDataPagesTest(@RequestBody Url[] list) throws InterruptedException {
        List<Pages> resultList = new ArrayList<>();

            ContentService contentService = new ContentService();
            resultList= contentService.getPageInfor(list);
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
    public List<RedirectionReport> getDataRedirectTest(@RequestBody Url[] list) throws IOException {
        ContentService con  = new ContentService();
        List<RedirectionReport> resultList = con.redirectionTest(list);
       linkRedirectionRepository.deleteAll();
       linkRedirectionRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/redirectiontest/lastest")
    public List<RedirectionReport> getLastestLinkRedirection()
    {
        List<RedirectionReport> resultList =  linkRedirectionRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/contactDetail")
    public List<ContactReport> getDataContactDetail(@RequestBody Url[] list) throws IOException {
        ContentService con  = new ContentService();
        List<ContactReport> resultList = con.getContactDetail(list);
        contactDetailRepository.deleteAll();
        contactDetailRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/contactDetail/lastest")
    public List<ContactReport> getLastestContactDetail()
    {

        List<ContactReport> resultList =  contactDetailRepository.findAll();
        return resultList;
    }
}
