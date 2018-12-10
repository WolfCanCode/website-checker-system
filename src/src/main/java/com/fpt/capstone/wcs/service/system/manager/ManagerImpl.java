package com.fpt.capstone.wcs.service.system.manager;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.capstone.wcs.controller.system.SitemapController;
import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.*;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsitePOJO;
import com.fpt.capstone.wcs.repository.user.RoleRepository;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import com.fpt.capstone.wcs.repository.website.*;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.utils.Constant;
import com.fpt.capstone.wcs.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;

@Service
public class ManagerImpl implements ManagerService{
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
                       VersionRepository versionRepository) {
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
                    PageOption existPageOption = pageOptionRepository.findOneByWebsiteAndNameEqualsAndDelFlagEqualsAndCreatedUser(tmp,"root", false, staff);
                    if(existPageOption == null)
                    {
                        PageOption root = new PageOption();
                        root.setName("root");
                        Version version = versionRepository.findFirstByWebsiteOrderByVersionDesc(tmp);
                        Page rootPage = pageRepository.findAllByWebsiteAndVersionAndUrlEquals(tmp,version , tmp.getUrl());
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
}
