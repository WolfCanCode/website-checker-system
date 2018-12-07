package com.fpt.capstone.wcs.controller.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.Topic;
import com.fpt.capstone.wcs.model.entity.website.Version;
import com.fpt.capstone.wcs.model.entity.website.WarningWord;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsitePOJO;
import com.fpt.capstone.wcs.repository.user.RoleRepository;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import com.fpt.capstone.wcs.repository.website.TopicRepository;
import com.fpt.capstone.wcs.repository.website.WarningWordRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.service.system.manager.ManagerService;
import com.fpt.capstone.wcs.utils.Constant;
import com.fpt.capstone.wcs.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.*;

@RestController
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @CrossOrigin
    @PostMapping("/api/website/manage")
    public Map<String, Object> getmanageWesite(@RequestBody ManagerRequestPOJO request) {
        return  managerService.getmanageWesite(request);
    }


    @CrossOrigin
    @PostMapping("/api/manager/addWebsite")
    public Map<String, Object> addWebsite(@RequestBody ManagerRequestPOJO request) throws MalformedURLException, JsonProcessingException {
        return  managerService.addWebsite(request);
    }

    @CrossOrigin
    @PostMapping("/api/manager/editWebsite")
    public Map<String, Object> editWebsite(@RequestBody ManagerRequestPOJO request) {
        return  managerService.editWebsite(request);
    }

    @CrossOrigin
    @PostMapping("/api/manager/deleteWebsite")
    public Map<String, Object> delWebsite(@RequestBody ManagerRequestPOJO request) {
        return  managerService.delWebsite(request);
    }

    @CrossOrigin
    @PostMapping("/api/manager/assignWebsite")
    public Map<String, Object> assignWebsite(@RequestBody ManagerRequestPOJO request) {
        return  managerService.assignWebsite(request);
    }

    @CrossOrigin
    @PostMapping("/api/manager/defaultAssign")
    public Map<String, Object> defaultAssWebsite(@RequestBody ManagerRequestPOJO request) {
        return  managerService.defaultAssWebsite(request);
    }

    @CrossOrigin
    @PostMapping("/api/user/getStaff")
    public Map<String, Object> getAllStaff(@RequestBody ManagerRequestPOJO request) {
        return  managerService.getAllStaff(request);
    }

    @CrossOrigin
    @PostMapping("/api/user/addStaff")
    public Map<String, Object> addStaff(@RequestBody ManagerRequestPOJO request) {
        return  managerService.addStaff(request);
    }

    @CrossOrigin
    @PostMapping("/api/user/editStaff")
    public Map<String, Object> editStaff(@RequestBody ManagerRequestPOJO request) {
        return  managerService.editStaff(request);
    }

    @CrossOrigin
    @PostMapping("api/user/deleteStaff")
    public Map<String, Object> deleteStaff(@RequestBody ManagerRequestPOJO request) {
        return  managerService.deleteStaff(request);
    }


    @CrossOrigin
    @PostMapping("/api/word/manage")
    public Map<String, Object> getWarningWordList(@RequestBody ManagerRequestPOJO request) {
        return  managerService.getWarningWordList(request);
    }

    @CrossOrigin
    @PostMapping("/api/word/getTopicList")
    public Map<String, Object> getTopicList(@RequestBody ManagerRequestPOJO request) {
        return  managerService.getTopicList(request);
    }


    @CrossOrigin
    @PostMapping("/api/word/addWarningWord")
    public Map<String, Object> addWarningWord(@RequestBody ManagerRequestPOJO request) throws MalformedURLException {
        return  managerService.addWarningWord(request);
    }

    @CrossOrigin
    @PostMapping("/api/word/editWarningWord")
    public Map<String, Object> editWarningWord(@RequestBody ManagerRequestPOJO request) {
        return  managerService.editWarningWord(request);
    }

    @CrossOrigin
    @PostMapping("/api/word/deleteWarningWord")
    public Map<String, Object> deleteWarningWord(@RequestBody ManagerRequestPOJO request) {
        return  managerService.deleteWarningWord(request);
    }


}
