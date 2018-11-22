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
import com.fpt.capstone.wcs.utils.Authenticate;
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
    @Autowired
    Authenticate authenticate;
//
//    @PostMapping("/api/sitemap")
//    public List<SiteMapOutputPOJO> getSiteMap(@RequestBody UrlPOJO url) throws MalformedURLException {
//        String urlRoot = url.getUrl();
//        System.out.println("ROOT: " + urlRoot);
//        SiteMapService sitemap = new SiteMapService(urlRoot);
//        sitemap.buildSiteMap();
//        List<String> rs = sitemap.getDecodeGraph();
//        SiteMapOutputPOJO sm = new SiteMapOutputPOJO();
//        sm.setMap(rs.get(0));
//        sm.setTypeMap(rs.get(1));
//        sm.setUrlMap(rs.get(2));
//        List<SiteMapOutputPOJO> res = new ArrayList<>();
//        res.add(sm);
//        return res;
//    }

    @PostMapping("/api/sitemap/getVisualSitemap")
    public List<SiteMapOutputPOJO> getSiteTree(@RequestBody RequestCommonPOJO request) throws MalformedURLException {
        List<SiteMapOutputPOJO> sm = null;
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            String url = website.getUrl();
            SiteMapService sms = new SiteMapService(url);
            sms.buildSiteMap();
            List<String> rs = sms.getDecodeGraph();
            sm = new ArrayList<>() ;
            sm.add(new SiteMapOutputPOJO(rs.get(0), rs.get(1), rs.get(2)));
        }
        return sm;
    }

    @PostMapping("/api/sitemap/getReferecingUrl")
    public List<SiteMapOutputPOJO> getSiteTree(@RequestBody RequestCommonPOJO request) throws MalformedURLException {
        List<SiteMapOutputPOJO> sm = null;
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            String url = website.getUrl();
            SiteMapService sms = new SiteMapService(url);
            sms.buildSiteMap();
            List<String> rs = sms.getDecodeGraph();
            sm = new ArrayList<>() ;
            sm.add(new SiteMapOutputPOJO(rs.get(0), rs.get(1), rs.get(2)));
        }
        return sm;
    }

    @CrossOrigin
    @PostMapping("/api/sitemap/getVer")
    public Map<String, Object> getLastestVer(@RequestBody RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            if (website != null) {
                Version ver = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
                if (ver == null) {
                    res.put("action", Constant.SUCCESS);
                    res.put("version", 0);
                    res.put("time", 0);
                    return res;
                } else {
                    res.put("action", Constant.SUCCESS);
                    res.put("version", ver.getVersion());
                    res.put("time", ver.getTime().getDate() + "/" + (ver.getTime().getMonth() + 1) + "/2018");

                    return res;
                }
            } else {
                res.put("action", Constant.INCORRECT);
                res.put("messages", "Something went wrong");
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("messages", "Auth is invalid");
            return res;
        }
    }

    @CrossOrigin
    @PostMapping("/api/sitemap/makeVer")
    public Map<String, Object> makeNewVer(@RequestBody RequestCommonPOJO request) throws MalformedURLException {
        Map<String, Object> res = new HashMap<>();
        User user = authenticate.isAuthGetSingleUser(request);
        if (user.getManager() == null) {
            Website website = websiteRepository.findOneByUserAndIdAndDelFlagEquals(user, request.getWebsiteId(), false);
            //Temp version
            Version ver = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
            Version verTmp = new Version();
            if (ver == null) {
                verTmp.setTime(new Date());
                verTmp.setVersion(1);
                verTmp.setWebsite(website);
                versionRepository.save(verTmp);
            } else {
                verTmp = new Version();
                verTmp.setTime(new Date());
                verTmp.setVersion(ver.getVersion() + 1);
                verTmp.setWebsite(website);
                versionRepository.save(verTmp);
            }
            ver = versionRepository.findVersionByWebsiteAndVersion(website, verTmp.getVersion());
            if (website != null) {
                SiteMapService sms = new SiteMapService(website.getUrl());
                sms.buildSiteMap();
                List<Page> pages = sms.getAllPage(website, ver);
                pageRepository.saveAll(pages);
                res.put("action", Constant.SUCCESS);
                res.put("version", ver.getVersion());
                res.put("time", ver.getTime().getDate() + "/" + (ver.getTime().getMonth() + 1) + "/2018");
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }

}
