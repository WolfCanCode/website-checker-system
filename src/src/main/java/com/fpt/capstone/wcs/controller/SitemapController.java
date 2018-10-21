package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.service.SiteMapService;
import com.fpt.capstone.wcs.model.SiteMapOutput;
import com.fpt.capstone.wcs.model.Url;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SitemapController {

    @PostMapping("/api/sitemap")
    public List<SiteMapOutput> getSiteMap(@RequestBody Url url) throws MalformedURLException {
        String urlRoot = url.getUrl();
        System.out.println("ROOT: " + urlRoot);
        SiteMapService sitemap = new SiteMapService(urlRoot);
        sitemap.buildSiteMap();
        List<String> rs = sitemap.getDecodeGraph();
        SiteMapOutput sm = new SiteMapOutput();
        sm.setMap(rs.get(0));
        sm.setTypeMap(rs.get(1));
        sm.setUrlMap(rs.get(2));
        List<SiteMapOutput> res = new ArrayList<>();
        res.add(sm);
        return res;
    }

}
