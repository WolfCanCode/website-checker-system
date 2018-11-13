package com.fpt.capstone.wcs.service.PageOption;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.Version;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.PageOptionPojo;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PageOptionImpl implements PageOptionService {
    final
    WebsiteRepository websiteRepository;
    final
    UserRepository userRepository;
    final
    Authenticate authenticate;
    final
    PageOptionRepository pageOptionRepository;
    final
    PageRepository pageRepository;
    private final VersionRepository versionRepository;

    @Autowired
    public PageOptionImpl(WebsiteRepository websiteRepository, UserRepository userRepository, Authenticate authenticate, PageOptionRepository pageOptionRepository, PageRepository pageRepository, VersionRepository versionRepository) {
        this.websiteRepository = websiteRepository;
        this.userRepository = userRepository;
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.pageRepository = pageRepository;
        this.versionRepository = versionRepository;
    }

    @Override
    public Map<String, Object> init(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption firstPageOption = pageOptionRepository.findFirstByWebsiteAndDelFlagEqualsOrderByTimeDesc(website, false);
            List<PageOption> pageOptions = pageOptionRepository.findAllByWebsiteAndDelFlagEqualsOrderByTimeDesc(website,false);
            Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(website);
            if (version != null) {
                List<Page> listPage = pageRepository.findAllByWebsiteAndVersionAndTypeEquals(website, version, 1);
                if (firstPageOption == null) {
                } else {
                    res.put("currentPageOption", firstPageOption.getPages());
                }
                res.put("allPageOption", pageOptions);
                res.put("listPage", listPage);
                res.put("websiteName", website.getUrl());
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                res.put("message", "Please generate sitemap first");
                return res;

            }
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> selectPageOption(PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption selPageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website,false);

            if (selPageOption == null) {
            } else {
                res.put("currentPageOption", selPageOption.getPages());
                res.put("websiteName", website.getUrl());
            }
            res.put("action", Constant.SUCCESS);
            return res;
        }
        else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> addPageOption(PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = new PageOption();
            pageOption.setWebsite(website);
            pageOption.setName(request.getPageOptionName());
            pageOption.setTime(new Date());
            pageOptionRepository.save(pageOption);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> updatePageOption(PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website,false);
            List<Page> pages = new ArrayList<>();
            for (int i = 0; i< request.getListPageId().size();i++){
                pages.add(pageRepository.findById(request.getListPageId().get(i)).get());
            }
            pageOption.setPages(pages);
            pageOptionRepository.save(pageOption);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> updateNamePageOption(PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(),website,false);
            pageOption.setName(request.getPageOptionName());
            pageOptionRepository.save(pageOption);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> deletePageOption(PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            pageOptionRepository.deleteById(request.getPageOptionId());
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> pageOptionSize(PageOptionPojo request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(),website,false);
            if(pageOption != null) {
                res.put("size",pageOption.getPages().size());
            }
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }
}
