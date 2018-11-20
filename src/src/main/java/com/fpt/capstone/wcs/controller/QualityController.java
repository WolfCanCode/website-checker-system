package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.Quality.QualityService;




import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class QualityController {

    @Autowired
    QualityService qualityService;



    @CrossOrigin
    @Transactional
    @PostMapping("/api/brokenLink")
    public Map<String, Object> getDataBrokenLink(@RequestBody RequestCommonPOJO request) throws InterruptedException {
    return qualityService.getDataBrokenLink(request);
    }


    @CrossOrigin
    @PostMapping("/api/brokenLink/lastest")
    public Map<String, Object> getLastestBrokenLink(@RequestBody RequestCommonPOJO request) {
        return qualityService.getLastestBrokenLink(request);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/brokenPage")
    public Map<String, Object> getDataBrokenPage(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return qualityService.getDataBrokenPage(request);
    }

    @CrossOrigin
    @PostMapping("/api/brokenPage/lastest")
    public Map<String, Object> getLastestBrokenPage(@RequestBody RequestCommonPOJO request) {
        return qualityService.getLastestBrokenPage(request);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/prohibitedContent")
    public Map<String, Object> getDataProhibitedContent(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return qualityService.getDataProhibitedContent(request);
    }

    @CrossOrigin
    @PostMapping("/api/prohibitedContent/lastest")
    public Map<String, Object> getLastestProhibitedContent(@RequestBody RequestCommonPOJO request) {
        return qualityService.getLastestProhibitedContent(request);
    }

    @Transactional
    @PostMapping("/api/missingtest")
   public Map<String, Object> getMissingFile(@RequestBody MissingFilePOJO request) throws InterruptedException {
        return qualityService.getMissingFile(request);
    }

    @CrossOrigin
    @PostMapping("/api/missingtest/lastest")
    public Map<String, Object> getLastestMissingFile(@RequestBody MissingFilePOJO request) {
        return  qualityService.getLastestMissingFile(request);
    }
}
