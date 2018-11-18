package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.pojo.PageOptionPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.service.Header.HeaderService;
import com.fpt.capstone.wcs.service.PageOption.PageOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ConfigController {

    final
    PageOptionService pageOptionService;
    final
    HeaderService headerService;


    @Autowired
    public ConfigController(PageOptionService pageOptionService, HeaderService headerService) {
        this.pageOptionService = pageOptionService;
        this.headerService = headerService;
    }

    @CrossOrigin
    @PostMapping("/api/headerStaff")
    public Map<String, Object> getHeaderWebsiteStaff(@RequestBody RequestCommonPOJO request) {
        return  headerService.headerStaff(request);
    }

    @CrossOrigin
    @PostMapping("/api/headerManager")
    public Map<String, Object> getHeaderWebsiteManager(@RequestBody RequestCommonPOJO request) {
        return  headerService.headerManager(request);
    }

    @CrossOrigin
    @PostMapping("/api/page/pageOption")
    public Map<String, Object> getPageOption(@RequestBody RequestCommonPOJO request) {
        return pageOptionService.init(request);
    }

    @CrossOrigin
    @PostMapping("/api/page/pageOption/select")
    public Map<String, Object> getSelectedPageOption(@RequestBody PageOptionPOJO request) {
        return  pageOptionService.selectPageOption(request);
    }

    @CrossOrigin
    @PostMapping("/api/page/pageOption/add")
    public Map<String, Object> addPageOption(@RequestBody PageOptionPOJO request) {
        return  pageOptionService.addPageOption(request);
    }

    @CrossOrigin
    @PostMapping("/api/page/pageOption/updatePage")
    public Map<String, Object> updatePageOption(@RequestBody PageOptionPOJO request) {
        return  pageOptionService.updatePageOption(request);
    }

    @CrossOrigin
    @PostMapping("/api/page/pageOption/updateName")
    public Map<String, Object> editPageOption(@RequestBody PageOptionPOJO request) {
        return  pageOptionService.updateNamePageOption(request);
    }

    @CrossOrigin
    @PostMapping("/api/page/pageOption/delete")
    public Map<String, Object> deletePageOption(@RequestBody PageOptionPOJO request) {
        return  pageOptionService.deletePageOption(request);
    }

    @CrossOrigin
    @PostMapping("/api/page/pageOption/size")
    public Map<String, Object> sizePageOption(@RequestBody PageOptionPOJO request) {
        return  pageOptionService.pageOptionSize(request);
    }

}
