package com.fpt.capstone.wcs.service.system.manager;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.capstone.wcs.controller.system.SitemapController;
import com.fpt.capstone.wcs.model.entity.report.content.ContactReport;
import com.fpt.capstone.wcs.model.entity.report.content.PageReport;
import com.fpt.capstone.wcs.model.entity.report.content.RedirectionReport;
import com.fpt.capstone.wcs.model.entity.report.experience.MobileLayoutReport;
import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenPageReport;
import com.fpt.capstone.wcs.model.entity.report.quality.MissingFileReport;
import com.fpt.capstone.wcs.model.entity.report.quality.ProhibitedContentReport;
import com.fpt.capstone.wcs.model.entity.report.technology.CookieReport;
import com.fpt.capstone.wcs.model.entity.report.technology.FaviconReport;
import com.fpt.capstone.wcs.model.entity.report.technology.JavascriptReport;
import com.fpt.capstone.wcs.model.entity.report.technology.ServerBehaviorReport;
import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.*;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
import com.fpt.capstone.wcs.model.pojo.ReportPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsitePOJO;
import com.fpt.capstone.wcs.repository.report.content.ContactDetailRepository;
import com.fpt.capstone.wcs.repository.report.content.LinkRedirectionRepository;
import com.fpt.capstone.wcs.repository.report.content.PageTestRepository;
import com.fpt.capstone.wcs.repository.report.experience.MobileLayoutRepository;
import com.fpt.capstone.wcs.repository.report.experience.SpeedtestRepository;
import com.fpt.capstone.wcs.repository.report.quality.BrokenLinkRepository;
import com.fpt.capstone.wcs.repository.report.quality.BrokenPageRepository;
import com.fpt.capstone.wcs.repository.report.quality.MissingFilesPagesRepository;
import com.fpt.capstone.wcs.repository.report.quality.ProhibitedContentRepository;
import com.fpt.capstone.wcs.repository.report.technology.CookieRepository;
import com.fpt.capstone.wcs.repository.report.technology.FaviconRepository;
import com.fpt.capstone.wcs.repository.report.technology.JSRepository;
import com.fpt.capstone.wcs.repository.report.technology.ServerBehaviorRepository;
import com.fpt.capstone.wcs.repository.user.RoleRepository;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import com.fpt.capstone.wcs.repository.website.*;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.utils.Constant;
import com.fpt.capstone.wcs.utils.EncodeUtil;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;

@Service
public class ManagerImpl implements ManagerService {
    final
    AuthenticateImpl authenticate;
    final
    UserRepository userRepository;
    final
    RoleRepository roleRepository;
    final
    WebsiteRepository websiteRepository;
    final
    SitemapController sitemapController;
    final
    WarningWordRepository warningWordRepository;
    final
    TopicRepository topicRepository;
    final
    PageOptionRepository pageOptionRepository;
    final
    PageRepository pageRepository;
    final
    VersionRepository versionRepository;
    final
    SpeedtestRepository speedtestRepository;
    final
    ContactDetailRepository contactDetailRepository;
    final
    LinkRedirectionRepository linkRedirectionRepository;
    final
    PageTestRepository pageTestRepository;
    final
    MobileLayoutRepository mobileLayoutRepository;
    final
    BrokenLinkRepository brokenLinkRepository;
    final
    BrokenPageRepository brokenPageRepository;
    final
    MissingFilesPagesRepository missingFilesPagesRepository;
    final
    ProhibitedContentRepository prohibitedContentRepository;
    final
    CookieRepository cookieRepository;
    final
    FaviconRepository faviconRepository;
    final
    JSRepository jsRepository;
    final
    ServerBehaviorRepository serverBehaviorRepository;

    @Autowired
    public ManagerImpl(AuthenticateImpl authenticate,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       WebsiteRepository websiteRepository,
                       SitemapController sitemapController,
                       WarningWordRepository warningWordRepository,
                       TopicRepository topicRepository,
                       PageOptionRepository pageOptionRepository,
                       PageRepository pageRepository,
                       VersionRepository versionRepository,
                       SpeedtestRepository speedtestRepository,
                       ContactDetailRepository contactDetailRepository,
                       LinkRedirectionRepository linkRedirectionRepository,
                       PageTestRepository pageTestRepository,
                       MobileLayoutRepository mobileLayoutRepository,
                       BrokenLinkRepository brokenLinkRepository,
                       BrokenPageRepository brokenPageRepository,
                       MissingFilesPagesRepository missingFilesPagesRepository,
                       ProhibitedContentRepository prohibitedContentRepository,
                       CookieRepository cookieRepository,
                       FaviconRepository faviconRepository,
                       JSRepository jsRepository,
                       ServerBehaviorRepository serverBehaviorRepository
    ) {
        this.authenticate = authenticate;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.websiteRepository = websiteRepository;
        this.sitemapController = sitemapController;
        this.warningWordRepository = warningWordRepository;
        this.topicRepository = topicRepository;
        this.pageOptionRepository = pageOptionRepository;
        this.pageRepository = pageRepository;
        this.versionRepository = versionRepository;

        this.speedtestRepository = speedtestRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.linkRedirectionRepository = linkRedirectionRepository;
        this.mobileLayoutRepository = mobileLayoutRepository;
        this.brokenLinkRepository = brokenLinkRepository;
        this.brokenPageRepository = brokenPageRepository;
        this.missingFilesPagesRepository = missingFilesPagesRepository;
        this.prohibitedContentRepository = prohibitedContentRepository;
        this.cookieRepository = cookieRepository;
        this.faviconRepository = faviconRepository;
        this.jsRepository = jsRepository;
        this.pageTestRepository = pageTestRepository;
        this.serverBehaviorRepository = serverBehaviorRepository;
    }

    @Override
    public Map<String, Object> getmanageWesite(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            List<Website> websites = websiteRepository.findAllByUserAndDelFlagEquals(manager, false);
            if (websites != null) {
                List<WebsitePOJO> websitePOJOS = new ArrayList<>();
                for (Website website : websites) {
                    WebsitePOJO web = new WebsitePOJO();
                    web.setId(website.getId());
                    web.setName(website.getName());
                    web.setUrl(website.getUrl());
                    List<Version> versions = website.getVersion();
                    if (versions != null) {
                        if (versions.size() != 0) {
                            Date time = versions.get(versions.size() - 1).getTime();
                            web.setVersion(versions.get(versions.size() - 1).getVersion());
                            web.setTime(time.getDate() + "/" + (time.getMonth() + 1) + "/2018");
                        } else {
                            web.setVersion(0);
                            web.setTime("Haven't get yet");
                        }
                    } else {
                        web.setVersion(0);
                        web.setTime("Haven't Test yet");
                    }
                    websitePOJOS.add(web);
                }
                res.put("action", Constant.SUCCESS);
                res.put("website", websitePOJOS);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> addWebsite(ManagerRequestPOJO request) throws MalformedURLException, JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Website tmp = websiteRepository
                    .findFirstByUserAndUrlAndDelFlagEqualsOrUserAndNameAndDelFlagEquals(
                            manager,
                            request.getWebsite().getUrl(),
                            false,
                            manager,
                            request.getWebsite().getName(),
                            false);
            if (tmp == null) {
                List<User> managers = new ArrayList<>();
                managers.add(manager);
                Website website = new Website();
                website.setName(request.getWebsite().getName());
                website.setUrl(request.getWebsite().getUrl());
                website.setUser(managers);
                websiteRepository.save(website);
                Website newWebsite = websiteRepository.findOneByUserAndUrlAndDelFlagEquals(manager, request.getWebsite().getUrl(), false);
                RequestCommonPOJO passAnotherService = new RequestCommonPOJO();
                passAnotherService.setWebsiteId(newWebsite.getId());
                passAnotherService.setUserId(request.getManagerId());
                passAnotherService.setUserToken(request.getManagerToken());
                res = sitemapController.makeNewVer(passAnotherService);
                return res;
            } else {
                res.put("action", Constant.DUPLICATE_ERROR);
                return res;
            }
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }

    @Override
    public Map<String, Object> editWebsite(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Website tmp = websiteRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWebsite().getId(), false);
            if (tmp != null) {
                tmp.setName(request.getWebsite().getName());
                websiteRepository.save(tmp);
                res.put("action", Constant.SUCCESS);
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

    @Override
    public Map<String, Object> delWebsite(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Website tmp = websiteRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWebsite().getId(), false);
            if (tmp != null) {
                tmp.setDelFlag(true);
                websiteRepository.save(tmp);
                res.put("action", Constant.SUCCESS);
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

    @Override
    public Map<String, Object> assignWebsite(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Website tmp = websiteRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWebsite().getId(), false);
            if (tmp != null) {
                List<User> listUser = userRepository.findAllByWebsiteAndDelFlagEquals(tmp, false);
                List<User> newList = new ArrayList<>();
                for (Long id : request.getListStaffId()) {
                    User staff = userRepository.findOneById(id);
                    newList.add(staff);
                }

                //loop delete
                for (int i = 0; i < listUser.size(); i++) {
                    boolean exist = false;
                    for (int j = 0; j < newList.size(); j++) {
                        if (listUser.get(i).getId() == newList.get(j).getId()) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        listUser.remove(listUser.get(i));
                    }
                    if (exist) {
                        exist = false;
                    }
                }

                //loop add
                for (int i = 0; i < newList.size(); i++) {
                    boolean exist = false;
                    for (int j = 0; j < listUser.size(); j++) {
                        if (newList.get(i).getId() == listUser.get(j).getId()) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        listUser.add(newList.get(i));
                    }
                    if (exist) {
                        exist = false;
                    }
                }

                tmp.setUser(listUser);
                websiteRepository.save(tmp);
                for (Long id : request.getListStaffId()) {
                    User staff = userRepository.findOneById(id);
                    PageOption existPageOption = pageOptionRepository.findOneByWebsiteAndNameEqualsAndDelFlagEqualsAndCreatedUser(tmp, "root", false, staff);
                    if (existPageOption == null) {
                        PageOption root = new PageOption();
                        root.setName("root");
                        Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(tmp);
                        Page rootPage = pageRepository.findAllByWebsiteAndVersionAndUrlEquals(tmp, version, tmp.getUrl());
                        List<Page> list = new ArrayList<>();
                        list.add(rootPage);
                        root.setPages(list);
                        root.setCreatedUser(staff);
                        root.setTime(new Date());
                        root.setWebsite(tmp);
                        root.setDelFlag(false);
                        pageOptionRepository.save(root);
                    }

                }

                res.put("action", Constant.SUCCESS);
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

    @Override
    public Map<String, Object> defaultAssWebsite(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Website tmp = websiteRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWebsite().getId(), false);
            if (tmp != null) {
                List<User> staff = userRepository.findAllByManagerAndDelFlagEquals(manager, false);

                res.put("staffs", staff);
                res.put("defStaffs", tmp.getUser());
                res.put("action", Constant.SUCCESS);
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

    @Override
    public Map<String, Object> getAllStaff(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            List<User> staff = userRepository.findAllByManagerAndDelFlagEquals(manager, false);
            for (int i = 0; i < staff.size(); i++) {
                staff.get(i).setWebsite(websiteRepository.findAllByUserAndDelFlagEquals(staff.get(i), false));
            }
            res.put("action", Constant.SUCCESS);
            res.put("listStaff", staff);
            return res;
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }

    @Override
    public Map<String, Object> addStaff(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        User addStaff = request.getStaff();
        if (manager != null) {
            addStaff.setManager(manager);
            addStaff.setRole(roleRepository.findById((long) 2).get());
            addStaff.setDelFlag(false);
            addStaff.setPassword(EncodeUtil.doEncode(addStaff.getPassword()));
            userRepository.save(addStaff);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }

    @Override
    public Map<String, Object> editStaff(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        User editStaff = request.getStaff();
        if (manager != null) {
            User staff = userRepository.findOneByIdAndDelFlagEquals(request.getStaff().getId(), false);
            staff.setEmail(editStaff.getEmail());
            staff.setName(editStaff.getName());
            if (editStaff.getPassword() != null && !editStaff.getPassword().equals("")) {
                staff.setPassword(EncodeUtil.doEncode(editStaff.getPassword()));
            }
            userRepository.save(staff);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }

    @Override
    public Map<String, Object> deleteStaff(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            User staff = userRepository.findOneByIdAndDelFlagEquals(request.getStaff().getId(), false);
            staff.setDelFlag(true);
            userRepository.save(staff);
            res.put("action", Constant.SUCCESS);
            return res;
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }

    @Override
    public Map<String, Object> getWarningWordList(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            List<WarningWord> warningWordList = warningWordRepository.findAllByUserAndDelFlagEquals(manager, false);
            if (warningWordList != null) {

                res.put("action", Constant.SUCCESS);
                res.put("warningWordList", warningWordList);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getTopicList(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            List<Topic> topicList = topicRepository.findAll();
            if (topicList != null) {

                res.put("action", Constant.SUCCESS);
                res.put("topicList", topicList);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> addWarningWord(ManagerRequestPOJO request) throws MalformedURLException {
        Map<String, Object> res = new HashMap<>();
        System.out.println(request);
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            WarningWord tmp = warningWordRepository
                    .findFirstByUserAndWordAndDelFlagEquals(
                            manager,
                            request.getWarningWord().getWord(),
                            false);

            if (tmp == null) {
                List<User> managers = new ArrayList<>();
                managers.add(manager);
                WarningWord warningWord = new WarningWord();
                warningWord.setWord(request.getWarningWord().getWord());
                //word.setWordType(wordTypeRepository.findByTypeName(request.getWord().getWordType().getTypeName()));
                warningWord.setUser(managers);
                //word.setWordType(request.getWarningWord().getWordType());
                warningWord.setTopic(request.getWarningWord().getTopic());
                warningWordRepository.save(warningWord);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("action", Constant.DUPLICATE_ERROR);
                return res;
            }
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }

    @Override
    public Map<String, Object> editWarningWord(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {


            WarningWord tmp = warningWordRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWarningWord().getId(), false);
            if (tmp != null) {
                tmp.setWord(request.getWarningWord().getWord());
                //tmp.setWordType(wordTypeRepository.findByTypeName(request.getWord().getWordType().getTypeName()));
                tmp.setTopic(request.getWarningWord().getTopic());
                warningWordRepository.save(tmp);
                res.put("action", Constant.SUCCESS);
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

    @Override
    public Map<String, Object> deleteWarningWord(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);

        if (manager != null) {
            WarningWord tmp = warningWordRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWarningWord().getId(), false);
            if (tmp != null) {
                tmp.setDelFlag(true);
                warningWordRepository.save(tmp);
                res.put("action", Constant.SUCCESS);
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

    @Override
    public Map<String, Object> getReportDetail(ManagerRequestPOJO request) {

        // nước tới zú rồi nên code chuối thông cảm

        Map<String, Object> res = new HashMap<>();
        Date date = new Date(request.getReportDate());
        PageOption pageOption = pageOptionRepository.findById(request.getPageOptionId()).get();
        switch (request.getReportType())
        {
            case "brokenLink":
                List<BrokenLinkReport> brokenLinkReports = brokenLinkRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",brokenLinkReports);
                break;
            case "brokenPage":
                List<BrokenPageReport> brokenPageReports = brokenPageRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",brokenPageReports);
                break;
            case "missingFile":
                List<MissingFileReport> missingFileReports = missingFilesPagesRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",missingFileReports);
                break;
            case "prohibited":
                List<ProhibitedContentReport> prohibitedContentReports = prohibitedContentRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",prohibitedContentReports);
                break;
            case "mobileLayout":
                List<MobileLayoutReport> mobileLayoutReports = mobileLayoutRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",mobileLayoutReports);
                break;
            case "speed":
                List<SpeedTestReport> speedTestReports = speedtestRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",speedTestReports);
                break;
            case "javascript":
                List<JavascriptReport> javascriptReports = jsRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",javascriptReports);
                break;
            case "favicon":
                List<FaviconReport> faviconReports = faviconRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",faviconReports);
                break;
            case "cookie":
                List<CookieReport> cookieReports = cookieRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",cookieReports);
                break;
            case "server":
                List<ServerBehaviorReport> serverBehaviorReports = serverBehaviorRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",serverBehaviorReports);
                break;
            case "contact":
                List<ContactReport> contactReports = contactDetailRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",contactReports);
                break;
            case "redirection":
                List<RedirectionReport> redirectionReports = linkRedirectionRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",redirectionReports);
                break;
            case "page":
                List<PageReport> pageReports = pageTestRepository.findAllByPageOptionAndCreatedTime(pageOption,date);
                res.put("data",pageReports);
                break;
        }
        res.put("action", Constant.SUCCESS);
        return res;
    }

    @Override
    public Map<String, Object> getAllReport(ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
        //Speed test
        List<SpeedTestReport> speedList = speedtestRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> speedTestList = new ArrayList<>();
        for (SpeedTestReport item : speedList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            speedTestList.add(report);
        }

        List<ContactReport> contactList = contactDetailRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> contactTestList = new ArrayList<>();
        for (ContactReport item : contactList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            contactTestList.add(report);
        }

        List<RedirectionReport> redirectionList = linkRedirectionRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> redirectionTestList = new ArrayList<>();
        for (RedirectionReport item : redirectionList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            redirectionTestList.add(report);
        }

        List<PageReport> pageList = pageTestRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> pageTestList = new ArrayList<>();
        for (PageReport item : pageList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            pageTestList.add(report);
        }

        List<MobileLayoutReport> mobileList = mobileLayoutRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> mobileTestList = new ArrayList<>();
        for (MobileLayoutReport item : mobileList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            mobileTestList.add(report);
        }

        List<BrokenLinkReport> brokenLinkList = brokenLinkRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> brokenLinkTestList = new ArrayList<>();
        for (BrokenLinkReport item : brokenLinkList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            brokenLinkTestList.add(report);
        }

        List<BrokenPageReport> brokenPageList = brokenPageRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> brokenPageTestList = new ArrayList<>();
        for (BrokenPageReport item : brokenPageList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            brokenPageTestList.add(report);
        }

        List<MissingFileReport> missingList = missingFilesPagesRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> missingTestList = new ArrayList<>();
        for (MissingFileReport item : missingList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            missingTestList.add(report);
        }

        List<ProhibitedContentReport> prohibitedList = prohibitedContentRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> prohibitedTestList = new ArrayList<>();
        for (ProhibitedContentReport item : prohibitedList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            prohibitedTestList.add(report);
        }

        List<CookieReport> cookieList = cookieRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> cookieTestList = new ArrayList<>();
        for (CookieReport item : cookieList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            cookieTestList.add(report);
        }

        List<FaviconReport> faviconList = faviconRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> faviconTestList = new ArrayList<>();
        for (FaviconReport item : faviconList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            faviconTestList.add(report);
        }

        List<JavascriptReport> jsList = jsRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> jsTestList = new ArrayList<>();
        for (JavascriptReport item : jsList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            jsTestList.add(report);
        }

        List<ServerBehaviorReport> serverList = serverBehaviorRepository.findAllGroupByCreatedTime();
        List<ReportPOJO> servertestList = new ArrayList<>();
        for (SpeedTestReport item : speedList) {
            ReportPOJO report = new ReportPOJO();
            report.setPageOptionId(item.getPageOption().getId());
            report.setPageOptionName(item.getPageOption().getName());
            report.setReportDate(item.getCreatedTime().getTime());
            report.setWebsiteId(item.getPageOption().getWebsite().getId());
            report.setWebsiteName(item.getPageOption().getWebsite().getName());
            report.setUserId(item.getPageOption().getCreatedUser().getId());
            report.setUserName(item.getPageOption().getCreatedUser().getName());
            servertestList.add(report);
        }

//        List<SpeedTestReport> speedList = speedtestRepository.findAllGroupByCreatedTime();
//        List<ReportPOJO> speedtestList = new ArrayList<>();
//        for(SpeedTestReport item : speedList)
//        {
//            ReportPOJO report = new ReportPOJO();
//            report.setPageOptionId(item.getPageOption().getId());
//            report.setPageOptionName(item.getPageOption().getName());
//            report.setReportDate(item.getCreatedTime());
//            report.setType("speedTest");
//            report.setWebsiteId(item.getPageOption().getWebsite().getId());
//            report.setWebsiteName(item.getPageOption().getWebsite().getName());
//            speedtestList.add(report);
//        }
//
//        List<SpeedTestReport> speedList = speedtestRepository.findAllGroupByCreatedTime();
//        List<ReportPOJO> speedtestList = new ArrayList<>();
//        for(SpeedTestReport item : speedList)
//        {
//            ReportPOJO report = new ReportPOJO();
//            report.setPageOptionId(item.getPageOption().getId());
//            report.setPageOptionName(item.getPageOption().getName());
//            report.setReportDate(item.getCreatedTime());
//            report.setType("speedTest");
//            report.setWebsiteId(item.getPageOption().getWebsite().getId());
//            report.setWebsiteName(item.getPageOption().getWebsite().getName());
//            speedtestList.add(report);
//        }


        Map<String, Object> data = new HashMap<>();
        data.put("contactTest", contactTestList);
        data.put("redirectionTest", redirectionTestList);
        data.put("pageTest", pageTestList);
        data.put("mobileLayoutTest", mobileTestList);
        data.put("speedTest", speedTestList);
        data.put("brokenLinkTest", brokenLinkTestList);
        data.put("brokenPageTest", brokenPageTestList);
        data.put("missingFileTest", missingTestList);
        data.put("prohibitedTest", prohibitedTestList);
        data.put("cookieTest", cookieTestList);
        data.put("faviconTest", faviconTestList);
        data.put("javascriptTest", jsTestList);
        data.put("serverBehaviorTest", servertestList);
        res.put("action", Constant.SUCCESS);
        res.put("data",data);


        return res;
        } else {
            res.put("action", Constant.PERMISSION_ERROR);
            return res;
        }
    }


}
