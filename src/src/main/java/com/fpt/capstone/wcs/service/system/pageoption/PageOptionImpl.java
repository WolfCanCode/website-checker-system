package com.fpt.capstone.wcs.service.system.pageoption;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.website.Version;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.pojo.PageOptionPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.repository.website.PageRepository;
import com.fpt.capstone.wcs.repository.website.VersionRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
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
    AuthenticateImpl authenticate;
    final
    PageOptionRepository pageOptionRepository;
    final
    PageRepository pageRepository;
    private final VersionRepository versionRepository;

    @Autowired
    public PageOptionImpl(WebsiteRepository websiteRepository, UserRepository userRepository, AuthenticateImpl authenticate, PageOptionRepository pageOptionRepository, PageRepository pageRepository, VersionRepository versionRepository) {
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
        WebsiteUserPOJO websiteUser = authenticate.isAuthGetUserAndWebsite(request);
        if (websiteUser != null) {
            PageOption firstPageOption = pageOptionRepository.findFirstByWebsiteAndCreatedUserAndDelFlagEqualsOrderByTimeDesc(websiteUser.getWebsite(), websiteUser.getUser(), false);
            List<PageOption> pageOptions = pageOptionRepository
                    .findAllByWebsiteAndCreatedUserAndDelFlagEqualsOrderByTimeAsc(
                            websiteUser.getWebsite(),
                            websiteUser.getUser(),
                            false);
            Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(websiteUser.getWebsite());
            if (version != null) {
                //internal
                List<Page> listPage = pageRepository.findAllByWebsiteAndVersionAndTypeEquals(websiteUser.getWebsite(), version, 1);
                if (firstPageOption == null) {
                } else {
                    res.put("currentPageOption", firstPageOption.getPages());
                }
                res.put("allPageOption", pageOptions);
                res.put("listPage", listPage);
                res.put("websiteName", websiteUser.getWebsite().getUrl());
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
    public Map<String, Object> selectPageOption(PageOptionPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption selPageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);

            if (selPageOption == null) {
            } else {
                res.put("isRoot", selPageOption.getName().equals("root"));
                res.put("currentPageOption", selPageOption.getPages());
                res.put("websiteName", website.getUrl());
            }
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> addPageOption(PageOptionPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        WebsiteUserPOJO websiteUser = authenticate.isAuthGetUserAndWebsite(req);
        if (websiteUser != null) {
            if (!request.getPageOptionName().toLowerCase().equals("root")) {
                PageOption pageOption = new PageOption();
                pageOption.setWebsite(websiteUser.getWebsite());
                pageOption.setName(request.getPageOptionName());
                pageOption.setCreatedUser(websiteUser.getUser());
                pageOption.setTime(new Date());
                PageOption addResult = pageOptionRepository.save(pageOption);
//                Version rootVer = versionRepository.findFirstByWebsiteOrderByVersionDesc(websiteUser.getWebsite());
//                Page rootPage = pageRepository.findFirstByUrlAndWebsiteAndVersion(websiteUser.getWebsite().getUrl(), websiteUser.getWebsite(), rootVer);
//                request.setPageOptionId(addResult.getId());
//                List<Long> rootPageList = new ArrayList<>();
//                rootPageList.add(rootPage.getId());
//                request.setListPageId(rootPageList);
//                updatePageOption(request);
                res.put("action", Constant.SUCCESS);
            } else {
                res.put("action", Constant.INCORRECT);
                res.put("message", "Cannot add page option with name root");
            }
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> updatePageOption(PageOptionPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if (!pageOption.getName().equals("root")) {
                List<Page> pages = new ArrayList<>();
                for (int i = 0; i < request.getListPageId().size(); i++) {
                    pages.add(pageRepository.findById(request.getListPageId().get(i)).get());
                }
                pageOption.setPages(pages);
                pageOptionRepository.save(pageOption);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("message", "Cannot add page to root");
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> updateNamePageOption(PageOptionPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if (!pageOption.getName().equals("root")) {
                pageOption.setName(request.getPageOptionName());
                pageOptionRepository.save(pageOption);
                res.put("action", Constant.SUCCESS);
            } else {
                res.put("message", "Cannot edit root pageoption");
                res.put("action", Constant.INCORRECT);
                return res;
            }
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> deletePageOption(PageOptionPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            Optional<PageOption> p = pageOptionRepository.findById(request.getPageOptionId());
            if (p.isPresent()) {
                if (!p.get().getName().equals("root")) {
                    PageOption delPageOption = p.get();
                    delPageOption.setDelFlag(true);
                    pageOptionRepository.save(delPageOption);
                } else {
                    res.put("action", Constant.INCORRECT);
                    res.put("message", "Cannot delete root page option");
                    return res;
                }
            }
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }

    @Override
    public Map<String, Object> pageOptionSize(PageOptionPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO req = new RequestCommonPOJO();
        req.setUserToken(request.getUserToken());
        req.setWebsiteId(request.getWebsiteId());
        req.setUserId(request.getUserId());
        Website website = authenticate.isAuthGetSingleSite(req);
        if (website != null) {
            User user = userRepository.findOneById(request.getUserId());
            if (request.getPageOptionId() != null && request.getPageOptionId() != -1) {
                PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
                if (pageOption != null) {
                    res.put("id", pageOption.getId());
                    res.put("size", pageOption.getPages().size());
                    res.put("name", pageOption.getName());
                } else {
                    pageOption = pageOptionRepository.findFirstByWebsiteAndDelFlagEqualsAndNameEqualsAndCreatedUserOrderByTimeDesc(website, false, "root", user);
                    res.put("id", pageOption.getId());
                    res.put("size", pageOption.getPages().size());
                    res.put("name", pageOption.getName());
                }
                //get root
                PageOption rootPageOption = pageOptionRepository.findFirstByWebsiteAndDelFlagEqualsAndNameEqualsAndCreatedUserOrderByTimeDesc(website, false, "root", user);
                res.put("rootId",rootPageOption.getId());
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                PageOption pageOption = pageOptionRepository.findFirstByWebsiteAndCreatedUserAndDelFlagEqualsOrderByTimeDesc(website,  user, false);
                res.put("id", pageOption.getId());
                res.put("size", pageOption.getPages().size());
                res.put("name", pageOption.getName());
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            res.put("message", "Auth invalid");
            return res;
        }
    }
}
