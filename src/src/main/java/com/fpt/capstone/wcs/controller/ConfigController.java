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

    @PostMapping("/api/website/all")
    public Map<String, Object> getAllWebsite4Test(@RequestBody User user) {
        Optional<User> result = userRepository.findById(user.getId());
        Map<String, Object> res = new HashMap<>();
        if (result.isPresent()) {
            List<Website> websites = websiteRepository.findAllByUser(result.get());
            res.put("action", Constant.SUCCESS);
            res.put("website", websites);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/website/manage")
    public Map<String, Object> getmanageWesite(@RequestBody RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        List<Website> websites = authenticate.isAuthList(request);
        if(websites!=null){
            List<WebsitePojo> websitePojos = new ArrayList<>();
            for (Website website : websites)
            {
                WebsitePojo web = new WebsitePojo();
                web.setId(website.getId());
                web.setName(website.getName());
                web.setUrl(website.getUrl());
                List<Version> versions = website.getVersion();
                if(versions!=null) {
                    if(versions.size()!=0) {
                        Date time = versions.get(versions.size() - 1).getTime();
                        web.setVersion(versions.get(versions.size() - 1).getVersion());
                        web.setTime(time.getDate() + "/" + (time.getMonth() + 1) + "/2018");
                    } else {
                        web.setVersion(0);
                        web.setTime("Haven't get yet");
                    }
                } else {
                    web.setVersion(0);
                    web.setTime("Haven't Test yet");
                }
                websitePojos.add(web);
            }
            res.put("action", Constant.SUCCESS);
            res.put("website", websitePojos);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @PostMapping("/api/page/page-option")
    public Map<String, Object> getPageOption(@RequestBody RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuth(request);
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
