package com.fpt.capstone.wcs.service.system.sitemap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.website.Sitemap;
import com.fpt.capstone.wcs.model.entity.website.Version;
import com.fpt.capstone.wcs.model.pojo.*;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.repository.website.PageRepository;
import com.fpt.capstone.wcs.repository.website.SitemapRepository;
import com.fpt.capstone.wcs.repository.website.VersionRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.service.system.sitemapProc.SiteMapProcService;
import com.fpt.capstone.wcs.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

@Service
public class SiteMapImpl implements SiteMapService {
    final WebsiteRepository websiteRepository;
    final UserRepository userRepository;
    final PageRepository pageRepository;
    final VersionRepository versionRepository;
    final AuthenticateImpl authenticate;
    final PageOptionRepository pageOptionRepository;
    final SitemapRepository sitemapRepository;
    String value = "";

    @Autowired
    public SiteMapImpl(WebsiteRepository websiteRepository, UserRepository userRepository, PageRepository pageRepository, VersionRepository versionRepository, AuthenticateImpl authenticate, PageOptionRepository pageOptionRepository, SitemapRepository sitemapRepository) {
        this.websiteRepository = websiteRepository;
        this.userRepository = userRepository;
        this.pageRepository = pageRepository;
        this.versionRepository = versionRepository;
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.sitemapRepository = sitemapRepository;
    }

    @Override
    public List<SiteMapOutputPOJO> getVisualSitemap(RequestCommonPOJO request) throws MalformedURLException {
        List<SiteMapOutputPOJO> sm = null;
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            Version ver = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
            Sitemap smData = sitemapRepository.findByWebsiteAndVersion(website, ver);
            sm = new ArrayList<>();
            sm.add(new SiteMapOutputPOJO(smData.getMap(), smData.getTypeMap(), smData.getUrlMap()));
        }
        return sm;
    }

    @Override
    public Map<String, Object> getPagesReferenceToThisURL(ReferencePOJO request) throws IOException {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommonPOJO = new RequestCommonPOJO(request.getUserId(), request.getUserToken(), request.getWebsiteId(), request.getPageOptionId());
        User user = authenticate.isAuthGetSingleUser(requestCommonPOJO);
        if (user.getManager() == null) {
            Website website = websiteRepository.findOneByUserAndIdAndDelFlagEquals(user, request.getWebsiteId(), false);
            if (website != null) {
                Version ver = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
                Sitemap smData = sitemapRepository.findByWebsiteAndVersion(website, ver);

                ObjectMapper mapper = new ObjectMapper();
                String jsonString = smData.getStructureValue();
                SitemapValuePOJO smVal = mapper.readValue(jsonString, SitemapValuePOJO.class);

                String curPage = "";
                List<SiteLinkPOJO> resultList = new ArrayList<>();
                List<VerticePOJO> vertices = smVal.getVertices();
                for (int i = 0; i < vertices.size(); i++) {
                    List<LinkPOJO> list = vertices.get(i).getRefTo();
                    for (int j = 0; j < list.size(); j++) {
                        resultList.add(new SiteLinkPOJO(curPage, list.get(j).getValue(), list.get(j).getType()));
                    }
                }

//                SiteMapProcService sms = new SiteMapProcService(website.getUrl());
//                sms.buildSiteMap();
//                sms.buildInverseGraph();
//                int id = sms.getUrlMap().get(request.getUrl());
//                List<SiteLinkPOJO> resultList = sms.getInvGraph().get(id);

                res.put("pageList", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        }
        res.put("action", Constant.INCORRECT);
        return res;
    }

    @Override
    public Map<String, Object> getUrlsReferencedByThisPage(ReferencePOJO request) throws IOException {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommonPOJO = new RequestCommonPOJO(request.getUserId(), request.getUserToken(), request.getWebsiteId(), request.getPageOptionId());
        User user = authenticate.isAuthGetSingleUser(requestCommonPOJO);
        if (user.getManager() == null) {
            Website website = websiteRepository.findOneByUserAndIdAndDelFlagEquals(user, request.getWebsiteId(), false);
            if (website != null) {
//                SiteMapProcService sms = new SiteMapProcService(website.getUrl());
//                sms.buildSiteMap();
//                int mapId = sms.getUrlMap().get(request.getUrl());
//                List<SiteLinkPOJO> resultList = sms.getGraph().get(mapId);
                Version ver = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
                Sitemap smData = sitemapRepository.findByWebsiteAndVersion(website, ver);

                ObjectMapper mapper = new ObjectMapper();
                String jsonString = smData.getStructureValue();
                SitemapValuePOJO smVal = mapper.readValue(jsonString, SitemapValuePOJO.class);

                String curPage = "";
                List<SiteLinkPOJO> resultList = new ArrayList<>();
                List<VerticePOJO> vertices = smVal.getVertices();
                for (int i = 0; i < vertices.size(); i++) {
                    List<LinkPOJO> list = vertices.get(i).getRefBy();
                    for (int j = 0; j < list.size(); j++) {
                        resultList.add(new SiteLinkPOJO(curPage, list.get(j).getValue(), list.get(j).getType()));
                    }
                }
                res.put("pageList", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        }
        res.put("action", Constant.INCORRECT);
        return res;
    }

    @Override
    public Map<String, Object> getLatestVer(RequestCommonPOJO request) {
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

    @Override
    public Map<String, Object> makeNewVer(RequestCommonPOJO request) throws MalformedURLException, JsonProcessingException {
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
                SiteMapProcService sms = new SiteMapProcService(website.getUrl());
                sms.buildSiteMap();
                sms.buildInverseGraph();

                value = sms.convertToJson(ver.getVersion());
                Sitemap sm = new Sitemap();
                List<String> decodeInfo = sms.getDecodeGraph();

                sm.setMap(decodeInfo.get(0));
                sm.setTypeMap(decodeInfo.get(1));
                sm.setUrlMap(decodeInfo.get(2));
                sm.setWebsite(website);
                sm.setVersion(ver);
                sm.setStructureValue(value);

                sitemapRepository.save(sm);

                List<Page> pages = sms.getAllPage(website, ver);
                //clear root duplicate
                for(int l = 0 ; l < pages.size();l++)
                {
                    if(pages.get(l).getUrl().equals(website.getUrl()+"/")){
                        pages.remove(l);
                        break;
                    }
                }
                // renew page option
                pageRepository.saveAll(pages);
                List<Page> newPages = pageRepository.findAllByVersion(ver);
                List<PageOption> pageOptionList = pageOptionRepository.findAllByWebsiteAndDelFlagEquals(website, false);
                if (pageOptionList.size() != 0) {
                    for (int i = 0; i < pageOptionList.size(); i++) {
                        PageOption item = pageOptionList.get(i);
                        List<Page> pagesOfPageOption = item.getPages();

                        //map find new url with new version pages
                        for (int k = 0; k < newPages.size(); k++) {
                            for (int h = 0; h < pagesOfPageOption.size(); h++) {
                                if (newPages.get(k).getUrl().equals(pagesOfPageOption.get(h).getUrl())) {
                                    pagesOfPageOption.set(h, newPages.get(k));
                                }
                            }
                        }
                        pageOptionRepository.delete(item);
                        item.setPages(pagesOfPageOption);
                        pageOptionRepository.save(item);
                    }
                } else {
                    List<User> listU = userRepository.findAllByWebsiteAndDelFlagEquals(website, false);
                    for (User u : listU) {
                        PageOption root = new PageOption();
                        root.setName("root");
                        Page rootPage = pageRepository.findAllByWebsiteAndVersionAndUrlEquals(website, ver, website.getUrl());
                        List<Page> list = new ArrayList<>();
                        list.add(rootPage);
                        root.setPages(list);
                        root.setCreatedUser(u);
                        root.setTime(new Date());
                        root.setWebsite(website);
                        root.setDelFlag(false);
                        pageOptionRepository.save(root);
                    }
                }
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
