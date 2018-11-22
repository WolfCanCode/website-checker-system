package com.fpt.capstone.wcs.service.Sitemap;

import java.net.MalformedURLException;
import java.util.List;

public interface SitemapService {
    public List<String> crawlHtmlSource(String url) throws MalformedURLException;
    public void getLinksRefrencingTo(String url); // list all page which input URL is referencing to
    public void buildSiteMap() throws MalformedURLException;


}
