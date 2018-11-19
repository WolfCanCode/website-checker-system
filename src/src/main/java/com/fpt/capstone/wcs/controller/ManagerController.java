package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Version;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.entity.Word;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsitePOJO;
import com.fpt.capstone.wcs.repository.RoleRepository;
import com.fpt.capstone.wcs.repository.UserRepository;
import com.fpt.capstone.wcs.repository.WebsiteRepository;
import com.fpt.capstone.wcs.repository.WordRepository;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import com.fpt.capstone.wcs.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.*;

@RestController
public class ManagerController {

    @Autowired
    Authenticate authenticate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    WebsiteRepository websiteRepository;
    @Autowired
    SitemapController sitemapController;
    @Autowired
    WordRepository wordRepository;


    @CrossOrigin
    @PostMapping("/api/website/manage")
    public Map<String, Object> getmanageWesite(@RequestBody ManagerRequestPOJO request) {
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


    @CrossOrigin
    @PostMapping("/api/manager/addWebsite")
    public Map<String, Object> addWebsite(@RequestBody ManagerRequestPOJO request) throws MalformedURLException {
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
                RequestCommonPOJO passAnotherController = new RequestCommonPOJO();
                passAnotherController.setWebsiteId(newWebsite.getId());
                passAnotherController.setUserId(request.getManagerId());
                passAnotherController.setUserToken(request.getManagerToken());
                res = sitemapController.makeNewVer(passAnotherController);
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

    @CrossOrigin
    @PostMapping("/api/manager/editWebsite")
    public Map<String, Object> editWebsite(@RequestBody ManagerRequestPOJO request) {
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

    @CrossOrigin
    @PostMapping("/api/manager/deleteWebsite")
    public Map<String, Object> delWebsite(@RequestBody ManagerRequestPOJO request) {
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

    @CrossOrigin
    @PostMapping("/api/manager/assignWebsite")
    public Map<String, Object> assignWebsite(@RequestBody ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Website tmp = websiteRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWebsite().getId(), false);
            List<User> listStaff = new ArrayList<>();
            boolean checkManager = false;
            for (Long id : request.getListStaffId()) {
                if (manager.getId() == id) {
                    checkManager = true;
                }
                User staff = userRepository.findOneById(id);
                listStaff.add(staff);
            }
            if (checkManager == false) {
                listStaff.add(manager);
            }

            if (tmp != null) {
                tmp.setUser(listStaff);
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

    @CrossOrigin
    @PostMapping("/api/manager/defaultAssign")
    public Map<String, Object> defaultAssWebsite(@RequestBody ManagerRequestPOJO request) {
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

    @CrossOrigin
    @PostMapping("/api/user/getStaff")
    public Map<String, Object> getAllStaff(@RequestBody ManagerRequestPOJO request) {
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

    @CrossOrigin
    @PostMapping("/api/user/addStaff")
    public Map<String, Object> addStaff(@RequestBody ManagerRequestPOJO request) {
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

    @CrossOrigin
    @PostMapping("/api/user/editStaff")
    public Map<String, Object> editStaff(@RequestBody ManagerRequestPOJO request) {
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

    @CrossOrigin
    @PostMapping("api/user/deleteStaff")
    public Map<String, Object> deleteStaff(@RequestBody ManagerRequestPOJO request) {
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


    @CrossOrigin
    @PostMapping("/api/word/manage")
    public Map<String, Object> getWordList(@RequestBody ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            List<Word> wordList = wordRepository.findAllByUserAndDelFlagEquals(manager, false);
            if (wordList != null) {

                res.put("action", Constant.SUCCESS);
                res.put("wordList", wordList);
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

    @CrossOrigin
    @PostMapping("/api/word/addWord")
    public Map<String, Object> addWord(@RequestBody ManagerRequestPOJO request) throws MalformedURLException {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Word tmp = wordRepository
                    .findFirstByUserAndWordAndDelFlagEquals(
                            manager,
                            request.getWord().getWord(),
                            false);

            if (tmp == null) {
                List<User> managers = new ArrayList<>();
                managers.add(manager);
                Word word = new Word();
                word.setWord(request.getWord().getWord());
                word.setUser(managers);
                wordRepository.save(word);
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

    @CrossOrigin
    @PostMapping("/api/word/editWord")
    public Map<String, Object> editWord(@RequestBody ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Word tmp = wordRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWord().getId(), false);
            if (tmp != null) {
                tmp.setWord(request.getWord().getWord());
                wordRepository.save(tmp);
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

    @CrossOrigin
    @PostMapping("/api/word/deleteWord")
    public Map<String, Object> delWord(@RequestBody ManagerRequestPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User manager = authenticate.isAuthAndManagerGet(request);
        if (manager != null) {
            Word tmp = wordRepository.findOneByUserAndIdAndDelFlagEquals(manager, request.getWord().getId(), false);
            if (tmp != null) {
                tmp.setDelFlag(true);
                wordRepository.save(tmp);
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
