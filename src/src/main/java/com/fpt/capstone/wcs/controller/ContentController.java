package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;

import com.fpt.capstone.wcs.service.Content.ContentService;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ContentController {

    @Autowired
    ContentService contentService;

    @Transactional
    @PostMapping("/api/pagestest")
    public Map<String, Object> getDataPagesTest(@RequestBody RequestCommonPOJO request) throws InterruptedException {
       return contentService.getDataPagesTest(request);
    }

    @PostMapping("/api/pagestest/lastest")
    public Map<String, Object> getLastestPageTest(@RequestBody RequestCommonPOJO request)
    {
       return  contentService.getLastestPageTest(request);
    }




    @Transactional
    @PostMapping("/api/redirectiontest")
    public Map<String, Object> getDataRedirectTest(@RequestBody RequestCommonPOJO request) throws InterruptedException, IOException {
      return  contentService.getDataRedirectTest(request);
    }



    @PostMapping("/api/redirectiontest/lastest")
    public Map<String, Object> getLastestLinkRedirection(@RequestBody RequestCommonPOJO request)
    {
       return contentService.getLastestLinkRedirection(request);
    }




    @Transactional
    @PostMapping("/api/contactDetail")
    public Map<String, Object> getDataContactDetail(@RequestBody RequestCommonPOJO request) throws InterruptedException, IOException {
        return contentService.getDataContactDetail(request);
    }



    @PostMapping("/api/contactDetail/lastest")
    public Map<String, Object> getLastestContactDetail(@RequestBody RequestCommonPOJO request)
    {
       return  contentService.getLastestContactDetail(request);
    }

}
