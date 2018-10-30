package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Version;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
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

    @PostMapping("/api/sitemap/getVer")
    public Map<String, Object> getLastestVer(@RequestBody RequestCommonPOJO item) {
        Map<String, Object> res = new HashMap<>();
        User user = userRepository.findOneByIdAndToken(item.getUserId(), item.getUserToken());
        Website website = websiteRepository.findOneByUserAndId(user, item.getWebsiteId());
        if (website != null) {
            Version ver = versionRepository.findVersionByWebsite(website);
            if(ver==null)
            {
                res.put("action", Constant.SUCCESS);
                res.put("version",0);
                res.put("time",0);
                return res;
            } else {
                res.put("action", Constant.SUCCESS);
                res.put("version",ver.getVersion());
                res.put("time",ver.getTime());

                return  res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("messages","The token is invalid");
            return res;
        }
    }

    @PostMapping("/api/sitemap/makeVer")
    public Map<String, Object> makeNewVer(@RequestBody RequestCommonPOJO item) throws MalformedURLException {
        Map<String, Object> res = new HashMap<>();
        User user = userRepository.findOneByIdAndToken(item.getUserId(),item.getUserToken());
        Website website = websiteRepository.findOneByUserAndId(user,item.getWebsiteId());
        //Temp version
        Version ver = versionRepository.findVersionByWebsite(website);
        Version verTmp = new Version();
        if(ver==null) {
            verTmp.setTime(new Date());
            verTmp.setVersion(1);
            verTmp.setWebsite(website);
            versionRepository.save(verTmp);
        } else
        {
            verTmp = new Version();
            verTmp.setTime(new Date());
            verTmp.setVersion(ver.getVersion()+1);
            verTmp.setWebsite(website);
            versionRepository.save(verTmp);
        }
        ver = versionRepository.findVersionByWebsiteAndVersion(website,verTmp.getVersion());
        if(website!=null) {
            SiteMapService sms = new SiteMapService(website.getUrl());
            sms.buildSiteMap();
            List<Page> pages = sms.getAllPage(website,ver);
            pageRepository.saveAll(pages);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

}
