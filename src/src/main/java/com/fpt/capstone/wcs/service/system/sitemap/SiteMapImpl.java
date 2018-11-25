package com.fpt.capstone.wcs.service.system.sitemap;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.website.Version;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.SiteMapOutputPOJO;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.repository.website.PageRepository;
import com.fpt.capstone.wcs.repository.website.VersionRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.service.system.sitemapProc.SiteMapProcService;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;

@Service
public class SiteMapImpl implements SiteMapService{

    final
    WebsiteRepository websiteRepository;
    final
    UserRepository userRepository;
    final
    PageRepository pageRepository;
    final
    VersionRepository versionRepository;
    final
    AuthenticateImpl authenticate;
    final
    PageOptionRepository pageOptionRepository;


    @Autowired
    public SiteMapImpl(WebsiteRepository websiteRepository, UserRepository userRepository, PageRepository pageRepository, VersionRepository versionRepository, AuthenticateImpl authenticate, PageOptionRepository pageOptionRepository) {
        this.websiteRepository = websiteRepository;
        this.userRepository = userRepository;
        this.pageRepository = pageRepository;
        this.versionRepository = versionRepository;
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
    }


    @Override
    public List<SiteMapOutputPOJO> getVisualSitemap(RequestCommonPOJO request) throws MalformedURLException {
        List<SiteMapOutputPOJO> sm = null;
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            String url = website.getUrl();
            SiteMapProcService sms = new SiteMapProcService(url);
            sms.buildSiteMap();
            List<String> rs = sms.getDecodeGraph();
            sm = new ArrayList<>() ;
            sm.add(new SiteMapOutputPOJO(rs.get(0), rs.get(1), rs.get(2)));
        }
        return sm;
    }

    @Override
    public List<SiteMapOutputPOJO> getAllLinksReferencingOnSelectetUrl(RequestCommonPOJO request) throws MalformedURLException {
        List<SiteMapOutputPOJO> sm = null;
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            String url = website.getUrl();
            SiteMapProcService sms = new SiteMapProcService(url);
            sms.buildSiteMap();
            List<String> rs = sms.getDecodeGraph();
            sm = new ArrayList<>() ;
            sm.add(new SiteMapOutputPOJO(rs.get(0), rs.get(1), rs.get(2)));
        }
        return sm;
    }

    @Override
    public Map<String, Object> getLastestVer(RequestCommonPOJO request) {
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
    public Map<String, Object> makeNewVer(RequestCommonPOJO request) throws MalformedURLException {
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
                List<Page> pages = sms.getAllPage(website, ver);

                // renew page option
                pageRepository.saveAll(pages);
                List<Page> newPages = pageRepository.findAllByVersion(ver);
                List<PageOption> pageOptionList = pageOptionRepository.findAllByWebsiteAndDelFlagEquals(website, false);
                for(int i = 0 ; i < pageOptionList.size(); i++)
                {
                    PageOption item = pageOptionList.get(i);
                    List<Page> pagesOfPageOption = item.getPages();

                    //map find new url with new version pages
                    for(int k = 0 ; k < newPages.size(); k++)
                    {
                        for(int h = 0 ; h < pagesOfPageOption.size(); h++)
                        {
                            if(newPages.get(k).getUrl().equals(pagesOfPageOption.get(h).getUrl()))
                            {
                                pagesOfPageOption.set(h,newPages.get(k));
                            }
                        }
                    }
                    pageOptionRepository.delete(item);
                    item.setPages(pagesOfPageOption);
                    pageOptionRepository.save(item);
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
