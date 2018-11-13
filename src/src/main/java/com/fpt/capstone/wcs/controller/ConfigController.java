package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.PageOptionPojo;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsitePojo;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.Header.HeaderInterFace;
import com.fpt.capstone.wcs.service.PageOption.PageOptionInterface;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ConfigController {

    final
    PageOptionInterface pageOptionInterface;
    final
    HeaderInterFace headerInterFace;

    @Autowired
    public ConfigController(PageOptionInterface pageOptionInterface, HeaderInterFace headerInterFace) {
        this.pageOptionInterface = pageOptionInterface;
        this.headerInterFace = headerInterFace;
    }

    @PostMapping("/api/headerStaff")
    public Map<String, Object> getHeaderWebsiteStaff(@RequestBody RequestCommonPOJO request) {
        return  headerInterFace.headerStaff(request);
    }

    @PostMapping("/api/headerManager")
    public Map<String, Object> getHeaderWebsiteManager(@RequestBody RequestCommonPOJO request) {
        return  headerInterFace.headerManager(request);
    }

    @PostMapping("/api/page/pageOption")
    public Map<String, Object> getPageOption(@RequestBody RequestCommonPOJO request) {
        return pageOptionInterface.init(request);
    }

    @PostMapping("/api/page/pageOption/select")
    public Map<String, Object> getSelectedPageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionInterface.selectPageOption(request);
    }

    @PostMapping("/api/page/pageOption/add")
    public Map<String, Object> addPageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionInterface.addPageOption(request);
    }

    @PostMapping("/api/page/pageOption/updatePage")
    public Map<String, Object> updatePageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionInterface.updatePageOption(request);
    }

    @PostMapping("/api/page/pageOption/updateName")
    public Map<String, Object> editPageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionInterface.updateNamePageOption(request);
    }

    @PostMapping("/api/page/pageOption/delete")
    public Map<String, Object> deletePageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionInterface.deletePageOption(request);
    }

    @PostMapping("/api/page/pageOption/size")
    public Map<String, Object> sizePageOption(@RequestBody PageOptionPojo request) {
        return  pageOptionInterface.pageOptionSize(request);
    }

}
