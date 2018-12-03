package com.fpt.capstone.wcs.controller.system;

import com.fpt.capstone.wcs.model.pojo.ReferencePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.service.system.sitemap.SiteMapService;
import com.fpt.capstone.wcs.model.pojo.SiteMapOutputPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.*;

@RestController
public class SitemapController {

    @Autowired
    SiteMapService siteMapService;

    @PostMapping("/api/sitemap/getVisualSitemap")
    public List<SiteMapOutputPOJO> getVisualSitemap(@RequestBody RequestCommonPOJO request) throws MalformedURLException {
        return siteMapService.getVisualSitemap(request);
    }

    @PostMapping("/api/sitemap/getRefTo")
    public Map<String, Object> getRefTo(@RequestBody ReferencePOJO request) throws MalformedURLException {
        return siteMapService.getPagesReferenceToThisURL(request);
    }

    @PostMapping("/api/sitemap/getRefBy")
    public Map<String, Object> getRefBy(@RequestBody ReferencePOJO request) throws MalformedURLException {
        return siteMapService.getUrlsReferencedByThisPage(request);
    }

    @CrossOrigin
    @PostMapping("/api/sitemap/getVer")
    public Map<String, Object> getLastestVer(@RequestBody RequestCommonPOJO request) {
        return siteMapService.getLatestVer(request);
    }

    @CrossOrigin
    @PostMapping("/api/sitemap/makeVer")
    public Map<String, Object> makeNewVer(@RequestBody RequestCommonPOJO request) throws MalformedURLException {
        return siteMapService.makeNewVer(request);
    }

}
