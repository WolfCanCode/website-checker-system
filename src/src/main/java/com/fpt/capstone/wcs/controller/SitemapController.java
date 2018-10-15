package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.SiteLink;
import com.fpt.capstone.wcs.model.SiteMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class SitemapController {

    @GetMapping("api/sitemap")
    public List<String> buildSiteMap(@RequestParam("site") String url) {
        SiteMap sitemap = new SiteMap(url);
        sitemap.buildSiteMap();
        List<String> rs = sitemap.getDecodeGraph();
        return rs;
    }

    @PostMapping("/api/run")
    public List<String> run(@RequestBody String url) {
        return Arrays.asList("a", "b");
    }

//    public List<List<SiteLink>>
}
