package com.fpt.capstone.wcs.service.Content;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.ContactDetailRepository;
import com.fpt.capstone.wcs.repository.LinkRedirectionRepository;
import com.fpt.capstone.wcs.repository.PageOptionRepository;
import com.fpt.capstone.wcs.repository.PageTestRepository;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ContentImpl implements  ContentService {

    final
    Authenticate authenticate;
    final
    PageOptionRepository pageOptionRepository;
    final ContactDetailRepository contactDetailRepository;
    final PageTestRepository pageTestRepository;
    final LinkRedirectionRepository linkRedirectionRepository;

    public ContentImpl(Authenticate authenticate, PageOptionRepository pageOptionRepository, ContactDetailRepository contactDetailRepository, PageTestRepository pageTestRepository, LinkRedirectionRepository linkRedirectionRepository) {
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.pageTestRepository = pageTestRepository;
        this.linkRedirectionRepository = linkRedirectionRepository;
    }

    @Override
    public Map<String, Object> getDataPagesTest(RequestCommonPOJO request) {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                com.fpt.capstone.wcs.service.ContentService exp = new com.fpt.capstone.wcs.service.ContentService();
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
                com.fpt.capstone.wcs.service.ContentService exp = new com.fpt.capstone.wcs.service.ContentService();
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

    @Override
    public Map<String, Object> getLastestPageTest(RequestCommonPOJO request) {
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

    @Override
    public Map<String, Object> getDataRedirectTest(RequestCommonPOJO request) {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                com.fpt.capstone.wcs.service.ContentService exp = new com.fpt.capstone.wcs.service.ContentService();
                List<RedirectionReport> resultList = null;

                    resultList = exp.redirectionTest(pages, pageOption);

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
                com.fpt.capstone.wcs.service.ContentService exp = new com.fpt.capstone.wcs.service.ContentService();
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

    @Override
    public Map<String, Object> getLastestLinkRedirection(RequestCommonPOJO request) {
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

    @Override
    public Map<String, Object> getDataContactDetail(RequestCommonPOJO request) {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                com.fpt.capstone.wcs.service.ContentService exp = new com.fpt.capstone.wcs.service.ContentService();
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
                com.fpt.capstone.wcs.service.ContentService exp = new com.fpt.capstone.wcs.service.ContentService();
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

    @Override
    public Map<String, Object> getLastestContactDetail(RequestCommonPOJO request) {

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
}
