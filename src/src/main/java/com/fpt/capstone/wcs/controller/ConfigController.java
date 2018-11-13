package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.pojo.PageOptionPojo;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.service.Header.HeaderService;
import com.fpt.capstone.wcs.service.PageOption.PageOptionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/api/headerStaff")
    public Map<String, Object> getHeaderWebsiteStaff(@RequestBody RequestCommonPOJO request) {
        return  headerService.headerStaff(request);
    }

    @PostMapping("/api/headerManager")
    public Map<String, Object> getHeaderWebsiteManager(@RequestBody RequestCommonPOJO request) {
        return  headerService.headerManager(request);
    }

    @PostMapping("/api/page/pageOption")
    public Map<String, Object> getPageOption(@RequestBody RequestCommonPOJO request) {
        return pageOptionService.init(request);
    }

    @PostMapping("/api/page/pageOption/select")
    public Map<String, Object> getSelectedPageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionService.selectPageOption(request);
    }

    @PostMapping("/api/page/pageOption/add")
    public Map<String, Object> addPageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionService.addPageOption(request);
    }

    @PostMapping("/api/page/pageOption/updatePage")
    public Map<String, Object> updatePageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionService.updatePageOption(request);
    }

    @PostMapping("/api/page/pageOption/updateName")
    public Map<String, Object> editPageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionService.updateNamePageOption(request);
    }

    @PostMapping("/api/page/pageOption/delete")
    public Map<String, Object> deletePageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionService.deletePageOption(request);
    }

    @PostMapping("/api/page/pageOption/size")
    public Map<String, Object> sizePageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionService.pageOptionSize(request);
    }

}
