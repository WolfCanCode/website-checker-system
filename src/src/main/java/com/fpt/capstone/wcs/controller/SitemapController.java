package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Version;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.NewVersionPOJO;
import com.fpt.capstone.wcs.repository.PageRepository;
import com.fpt.capstone.wcs.repository.UserRepository;
import com.fpt.capstone.wcs.repository.VersionRepository;
import com.fpt.capstone.wcs.repository.WebsiteRepository;
import com.fpt.capstone.wcs.service.SiteMapService;
import com.fpt.capstone.wcs.model.pojo.SiteMapOutputPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.*;

@RestController
public class SitemapController {

    @Autowired
    WebsiteRepository websiteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;

    @PostMapping("/api/sitemap")
    public List<SiteMapOutputPOJO> getSiteMap(@RequestBody UrlPOJO url) throws MalformedURLException {
        String urlRoot = url.getUrl();
        System.out.println("ROOT: " + urlRoot);
        SiteMapService sitemap = new SiteMapService(urlRoot);
        sitemap.buildSiteMap();
        List<String> rs = sitemap.getDecodeGraph();
        SiteMapOutputPOJO sm = new SiteMapOutputPOJO();
        sm.setMap(rs.get(0));
        sm.setTypeMap(rs.get(1));
        sm.setUrlMap(rs.get(2));
        List<SiteMapOutputPOJO> res = new ArrayList<>();
        res.add(sm);
        return res;
    }

    @PostMapping("/api/sitemap/getNewVer")
    public Map<String, Object> getNewVer(@RequestBody NewVersionPOJO item) throws MalformedURLException {
        Map<String, Object> res = new HashMap<>();
        User user = userRepository.findOneByIdAndToken(item.getUserId(),item.getUserToken());
        Website website = websiteRepository.findOneByUserAndId(user,item.getWebsiteId());
        //Temp version
        Version ver = new Version();
        ver.setTime(new Date());
        ver.setVersion(1);
        versionRepository.save(ver);
        ver = versionRepository.findAll().get(0);
        if(website!=null) {
            SiteMapService sms = new SiteMapService(website.getUrl());
            sms.buildSiteMap();
            List<Page> pages = sms.getAllPage(website,ver);
            pageRepository.saveAll(pages);
            pages = pageRepository.findAllByWebsite(website);

            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

}
