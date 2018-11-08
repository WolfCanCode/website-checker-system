package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
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
        if (websites!= null) {
            res.put("action", Constant.SUCCESS);
            res.put("website", websites);
            res.put("fullname",user.getName());
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
        if (websites!= null) {
            res.put("action", Constant.SUCCESS);
            res.put("fullname",user.getName());
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
            PageOption pageOption = pageOptionRepository.findFirstByWebsiteOrderByTimeDesc(website);
            Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
            if(version!=null) {
                List<Page> listPage = pageRepository.findAllByWebsiteAndVersionAndTypeEquals(website, version,1);
                if (pageOption == null) {
                    res.put("pageNum", 0);
                } else {
                    res.put("pageNum", pageOption.getOptionNumber());
                    res.put("currentPageOption", pageOption.getPages());
                }
                res.put("listPage", listPage);
                res.put("action",Constant.SUCCESS);
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


}
