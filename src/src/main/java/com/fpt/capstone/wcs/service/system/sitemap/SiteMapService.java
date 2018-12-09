package com.fpt.capstone.wcs.service.system.sitemap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.capstone.wcs.model.pojo.ReferencePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.SiteMapOutputPOJO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface SiteMapService {

    public List<SiteMapOutputPOJO> getVisualSitemap(RequestCommonPOJO request) throws MalformedURLException;
    public Map<String, Object> getPagesReferenceToThisURL(ReferencePOJO request) throws IOException;
    public Map<String, Object> getUrlsReferencedByThisPage(ReferencePOJO request) throws IOException;;
    public Map<String, Object> getLatestVer(RequestCommonPOJO request);
    public Map<String, Object> makeNewVer(RequestCommonPOJO request) throws MalformedURLException, JsonProcessingException;
}
