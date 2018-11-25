package com.fpt.capstone.wcs.service.system.sitemap;

import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.SiteMapOutputPOJO;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface SiteMapService {

    public List<SiteMapOutputPOJO> getVisualSitemap(RequestCommonPOJO request) throws MalformedURLException;
    public List<SiteMapOutputPOJO> getAllLinksReferencingOnSelectetUrl(RequestCommonPOJO request) throws MalformedURLException;
    public Map<String, Object> getLastestVer(RequestCommonPOJO request);
    public Map<String, Object> makeNewVer(RequestCommonPOJO request) throws MalformedURLException;
}
