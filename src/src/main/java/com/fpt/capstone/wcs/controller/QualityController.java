package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.Quality.QualityService;




import com.fpt.capstone.wcs.utils.Authenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class QualityController {

    @Autowired
    PageRepository pageRepository;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    PageOptionRepository pageOptionRepository;
    @Autowired
    MissingFilesPagesRepository missingFilesPagesRepository;
    @Autowired
    QualityService qualityService;


    @Autowired
    Authenticate authenticate;


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
    @PostMapping("/api/missingtest")
    public List<MissingFileReport> getMissingFile(@RequestBody MissingFilePOJO[] request) throws InterruptedException {
        com.fpt.capstone.wcs.service.QualityService qualityService = new com.fpt.capstone.wcs.service.QualityService();
        List<MissingFileReport> result= new ArrayList<>();
        System.out.println( request.length);
        UrlPOJO[] list = new UrlPOJO[2];
        list[0]= new UrlPOJO("https://www.bhcosmetics.com/");
        list[1]= new UrlPOJO("https://thanhnien.vn/");
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
            for (MissingFilePOJO missingFilePOJODTO :request){
               int typeSwicth = missingFilePOJODTO.getType();
                System.out.println(typeSwicth);
                switch (missingFilePOJODTO.getType()){
                    case 1:{
                        result.addAll(qualityService.getMissingFileImg(list, urlRoot));
                        break;
                    }
                    case 2:{
                        result.addAll(qualityService.getMissingFileCss(list,urlRoot));
                        break;
                    }
                    case 3:{
                        result.addAll(qualityService.getMissingFileDoc(list, urlRoot));
                        break;

                    }
                    case 4:{
                        result.addAll(qualityService.getMissingFileARCHIVES(list, urlRoot));
                        break;
                    }
                }
            }
        }


        missingFilesPagesRepository.deleteAll();
        missingFilesPagesRepository.saveAll(result);
        System.out.println("Ket Thuc check");
        return result;
    }

    @CrossOrigin
    @PostMapping("/api/missingtest/lastest")
    public List<MissingFileReport> getLastestMissingFile() {
        List<MissingFileReport> result = missingFilesPagesRepository.findAll();
        return result;
    }
}
