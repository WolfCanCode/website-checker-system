package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.PageOptionPojo;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsitePojo;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ConfigController {

    @Autowired
    WebsiteRepository websiteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Authenticate authenticate;
    @Autowired
    PageOptionRepository pageOptionRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;

    @PostMapping("/api/headerStaff")
    public Map<String, Object> getHeaderWebsiteStaff(@RequestBody RequestCommonPOJO request) {
        List<Website> websites = authenticate.isAuthGetListSite(request);
        User user = userRepository.findById(request.getUserId()).get();
        Map<String, Object> res = new HashMap<>();
        if (websites != null) {
            res.put("action", Constant.SUCCESS);
            res.put("website", websites);
            res.put("fullname", user.getName());
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/headerManager")
    public Map<String, Object> getHeaderWebsiteManager(@RequestBody RequestCommonPOJO request) {
        List<Website> websites = authenticate.isAuthGetListSite(request);
        User user = userRepository.findById(request.getUserId()).get();
        Map<String, Object> res = new HashMap<>();
        if (websites != null) {
            res.put("action", Constant.SUCCESS);
            res.put("fullname", user.getName());
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/page/pageOption")
    public Map<String, Object> getPageOption(@RequestBody RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption firstPageOption = pageOptionRepository.findFirstByWebsiteAndDelFlagEqualsOrderByTimeDesc(website, false);
            List<PageOption> pageOptions = pageOptionRepository.findAllByWebsiteAndDelFlagEqualsOrderByTimeDesc(website,false);
            Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
            if (version != null) {
                List<Page> listPage = pageRepository.findAllByWebsiteAndVersionAndTypeEquals(website, version, 1);
                if (firstPageOption == null) {
                } else {
                    res.put("currentPageOption", firstPageOption.getPages());
                }
                res.put("allPageOption", pageOptions);
                res.put("listPage", listPage);
                res.put("websiteName", website.getUrl());
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                res.put("message", "Please generate sitemap first");
                return res;

            }
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @PostMapping("/api/page/pageOption/select")
    public Map<String, Object> getSelectedPageOption(@RequestBody PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption selPageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website,false);

                if (selPageOption == null) {
                } else {
                    res.put("currentPageOption", selPageOption.getPages());
                    res.put("websiteName", website.getUrl());
                }
                res.put("action", Constant.SUCCESS);
                return res;
            }
         else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @PostMapping("/api/page/pageOption/add")
    public Map<String, Object> addPageOption(@RequestBody PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = new PageOption();
            pageOption.setWebsite(website);
            pageOption.setName(request.getPageOptionName());
            pageOption.setTime(new Date());
            pageOptionRepository.save(pageOption);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @PostMapping("/api/page/pageOption/updatePage")
    public Map<String, Object> updatePageOption(@RequestBody PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website,false);
            List<Page> pages = new ArrayList<>();
            for (int i = 0; i< request.getListPageId().size();i++){
                pages.add(pageRepository.findById(request.getListPageId().get(i)).get());
            }
            pageOption.setPages(pages);
            pageOptionRepository.save(pageOption);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @PostMapping("/api/page/pageOption/updateName")
    public Map<String, Object> editPageOption(@RequestBody PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(),website,false);
            pageOption.setName(request.getPageOptionName());
            pageOptionRepository.save(pageOption);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @PostMapping("/api/page/pageOption/delete")
    public Map<String, Object> deletePageOption(@RequestBody PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            pageOptionRepository.deleteById(request.getPageOptionId());
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @PostMapping("/api/page/pageOption/size")
    public Map<String, Object> sizePageOption(@RequestBody PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(),website,false);
            if(pageOption != null) {
                res.put("size",pageOption.getPages().size());
            }
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

}
