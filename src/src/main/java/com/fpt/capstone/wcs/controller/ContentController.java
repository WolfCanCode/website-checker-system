package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.Pages;
import com.fpt.capstone.wcs.model.SpeedTest;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.service.ContentService;
import com.fpt.capstone.wcs.service.ExperienceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ContentController {
    @PostMapping("/api/pagestest")
    public List<Pages> getDataPagesTest(@RequestBody Url[] list) throws IOException {
        ContentService con  = new ContentService();
        List<Pages> resultList = new ArrayList<>();
        for(int i = 0 ; i< list.length; i++) {
            String title  = con.getTitle(list[i].url);
            int  httpcode = con.getStatus(list[i].url);
            String canoUrl = con.getCanonicalUrl(list[i].url);
            Pages  item = new Pages(list[i].url,title,canoUrl, httpcode);
            resultList.add(item);
        }

        return resultList;
    }
}
