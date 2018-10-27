package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.MissingFileDTO;
import com.fpt.capstone.wcs.model.entity.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.BrokenPageReport;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.model.entity.MissingFileReport;
import com.fpt.capstone.wcs.repository.BrokenLinkRepository;
import com.fpt.capstone.wcs.repository.BrokenPageRepository;
import com.fpt.capstone.wcs.repository.MissingFilesPagesRepository;
import com.fpt.capstone.wcs.service.QualityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class QualityController {

    @Autowired
    BrokenPageRepository brokenPageRepository;
    @Autowired
    BrokenLinkRepository brokenLinkRepository;

    @Autowired
    MissingFilesPagesRepository missingFilesPagesRepository;

    @PostMapping("/api/brokenLink")
    public List<BrokenLinkReport> getDataBrokenLink(@RequestBody Url[] list) throws InterruptedException {
        QualityService qlt = new QualityService();
        List<BrokenLinkReport> resultList = qlt.brokenLinkService(list);
        brokenLinkRepository.deleteAll();
        brokenLinkRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/brokenLink/lastest")
    public List<BrokenLinkReport> getLastestBrokenLink()
    {
        List<BrokenLinkReport> resultList = brokenLinkRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/brokenPage")
    public List<BrokenPageReport> getDataBrokenPage(@RequestBody Url[] list) throws InterruptedException {
        QualityService qlt = new QualityService();
        List<BrokenPageReport> resultList = qlt.brokenPageService(list);
        brokenPageRepository.deleteAll();
        brokenPageRepository.saveAll(resultList);
        return resultList;
    }

    @PostMapping("/api/brokenPage/lastest")
    public List<BrokenPageReport> getLastestBrokenPage()
    {
        List<BrokenPageReport> resultList = brokenPageRepository.findAll();
        return resultList;
    }

    @PostMapping("/api/missingtest")
    public List<MissingFileReport> getMissingFile(@RequestBody MissingFileDTO[] request) throws InterruptedException {
        QualityService qualityService = new QualityService();
        List<MissingFileReport> result= new ArrayList<>();
        System.out.println( request);
        Url[] list = new Url[2];
        list[0]= new Url("https://www.bhcosmetics.com/");
        list[1]= new Url("https://www.bhcosmetics.com/");
        String urlRoot="";
        for(int i =0; i< list.length;i++ ){
            Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(list[i].getUrl());
            while (matcher.find()){
                urlRoot = matcher.group();
            }
        }
        if(request.length==0|| request.length==4){
            System.out.println("Vo ne");
            result =  qualityService.getMissingFile(list, urlRoot);
        }
        else{
            for (MissingFileDTO missingFileDTO:request){
                switch (missingFileDTO.getType()){
                    case 1:{
                        result.addAll(qualityService.getMissingFileImg(list, urlRoot));
                    }
                    case 2:{
                        result.addAll(qualityService.getMissingFileCss(list,urlRoot));
                    }
                    case 3:{
                        result.addAll(qualityService.getMissingFileDoc(list, urlRoot));

                    }
                    case 4:{
                        result.addAll(qualityService.getMissingFileARCHIVES(list, urlRoot));
                    }
                }
            }
        }


        missingFilesPagesRepository.deleteAll();
        missingFilesPagesRepository.saveAll(result);
        System.out.println("Ket Thuc check");
        return result;
    }

    @PostMapping("/api/missingtest/lastest")
    public List<MissingFileReport> getLastestMissingFile() {
        List<MissingFileReport> result = missingFilesPagesRepository.findAll();
        return result;
    }
}
