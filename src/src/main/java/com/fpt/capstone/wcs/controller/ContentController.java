package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.ContentService;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    PageOptionRepository pageOptionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebsiteRepository websiteRepository;
    @Autowired
    Authenticate authenticate;



//    @PostMapping("/api/pagestest")
//    public List<PageReport> getDataPagesTest(@RequestBody UrlPOJO[] list) throws InterruptedException {
//        List<PageReport> resultList = new ArrayList<>();
//
//            ContentService contentService = new ContentService();
//            resultList= contentService.getPageInfor(list);
//            pageTestRepository.deleteAll();
//            pageTestRepository.saveAll(resultList);
//
//        return resultList;
//    }

    @PostMapping("/api/pagestest")
    public Map<String, Object> getDataPagesTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                ContentService exp = new ContentService();
                List<PageReport> resultList = exp.getPageInfor(pages, pageOption);
                pageTestRepository.removeAllByPageOption(pageOption);
                pageTestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("pagetestReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                ContentService exp = new ContentService();
                List<PageReport> resultList = exp.getPageInfor(pages, null);
                pageTestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("pagetestReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/pagestest/lastest")
    public Map<String, Object> getLastestPageTest(@RequestBody RequestCommonPOJO request)
    {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                List<PageReport> resultList = pageTestRepository.findAllByPageOption(pageOption);
                res.put("pagetestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<PageReport> resultList = pageTestRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("pagetestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @GetMapping("/api/pagestest/isExist")
    public boolean checkAlpagereadyCheck() {
        boolean success = pageTestRepository.findAll().isEmpty();
        return success;
    }

//    @PostMapping("/api/redirectiontest")
//    public List<RedirectionReport> getDataRedirectTest(@RequestBody UrlPOJO[] list) throws IOException {
//        ContentService con  = new ContentService();
//        List<RedirectionReport> resultList = con.redirectionTest(list);
//       linkRedirectionRepository.deleteAll();
//       linkRedirectionRepository.saveAll(resultList);
//        return resultList;
//    }

    @PostMapping("/api/redirectiontest")
    public Map<String, Object> getDataRedirectTest(@RequestBody RequestCommonPOJO request) throws InterruptedException, IOException {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                ContentService exp = new ContentService();
                List<RedirectionReport> resultList = null;
                try {
                    resultList = exp.redirectionTest(pages, pageOption);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                linkRedirectionRepository.removeAllByPageOption(pageOption);
                linkRedirectionRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("redirectiontestReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                ContentService exp = new ContentService();
                List<RedirectionReport> resultList = exp. redirectionTest(pages, null);
                linkRedirectionRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("redirectiontestReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

//    @PostMapping("/api/redirectiontest/lastest")
//    public List<RedirectionReport> getLastestLinkRedirection()
//    {
//        List<RedirectionReport> resultList =  linkRedirectionRepository.findAll();
//        return resultList;
//    }

    @PostMapping("/api/redirectiontest/lastest")
    public Map<String, Object> getLastestLinkRedirection(@RequestBody RequestCommonPOJO request)
    {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                List<RedirectionReport> resultList = linkRedirectionRepository.findAllByPageOption(pageOption);
                res.put("redirectiontestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<RedirectionReport> resultList = linkRedirectionRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("redirectiontestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @GetMapping("/api/redirectiontest/isExist")
    public boolean checkAlRedirectreadyCheck() {
        boolean success = linkRedirectionRepository.findAll().isEmpty();
        return success;
    }

//    @PostMapping("/api/contactDetail")
//    public List<ContactReport> getDataContactDetail(@RequestBody UrlPOJO[] list) throws IOException {
//        ContentService con  = new ContentService();
//        List<ContactReport> resultList = con.getContactDetail(list);
//        contactDetailRepository.deleteAll();
//        contactDetailRepository.saveAll(resultList);
//        return resultList;
//    }


    @PostMapping("/api/contactDetail")
    public Map<String, Object> getDataContactDetail(@RequestBody RequestCommonPOJO request) throws InterruptedException, IOException {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                ContentService exp = new ContentService();
                List<ContactReport> resultList = null;
                resultList = exp.getContactDetail(pages, pageOption);
                contactDetailRepository.removeAllByPageOption(pageOption);
                contactDetailRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("contactReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                ContentService exp = new ContentService();
                List<ContactReport> resultList = exp. getContactDetail(pages, null);
                contactDetailRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("contactReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

//    @PostMapping("/api/contactDetail/lastest")
//    public List<ContactReport> getLastestContactDetail()
//    {
//
//        List<ContactReport> resultList =  contactDetailRepository.findAll();
//        return resultList;
//    }

    @PostMapping("/api/contactDetail/lastest")
    public Map<String, Object> getLastestContactDetail(@RequestBody RequestCommonPOJO request)
    {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                List<ContactReport> resultList = contactDetailRepository.findAllByPageOption(pageOption);
                res.put("contactReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<ContactReport> resultList = contactDetailRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("contactReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @GetMapping("/api/contactDetail/isExist")
    public boolean checkAlContactreadyCheck() {
        boolean success = contactDetailRepository.findAll().isEmpty();
        return success;
    }
}
