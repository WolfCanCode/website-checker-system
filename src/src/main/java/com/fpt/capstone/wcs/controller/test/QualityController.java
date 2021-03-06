package com.fpt.capstone.wcs.controller.test;

import com.fpt.capstone.wcs.model.entity.report.quality.SpellingReport;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import com.fpt.capstone.wcs.model.pojo.SpellingSuggestionRequestPOJO;
import com.fpt.capstone.wcs.service.report.quality.QualityService;


import com.fpt.capstone.wcs.service.spelling.SpellingTestService;
import com.fpt.capstone.wcs.service.system.trietree.TrieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class QualityController {

    @Autowired
    QualityService qualityService;

    @Autowired
    TrieService trieService;

    @Autowired
    SpellingTestService spellingTestService;
//
//    @CrossOrigin
//    @Transactional
//    @GetMapping("/api/spelling")
//    public boolean checkExistWord(@RequestParam("word") String word) throws InterruptedException {
//        return trieService.isExistInDictionary(word);
//    }
//
//    @CrossOrigin
//    @Transactional
//    @GetMapping("/api/testNsuggest")
//    public List<SpellingReport> checkExistWord() throws InterruptedException {
//
//        return spellingTestService.testSpelling(Arrays.asList(), trieService);
//    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/spelling/getWrongWords")
    public Map<String, Object> getSpellingMistake(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return spellingTestService.getSpellingMistakes(request, trieService);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/spelling/getSuggestion")
    public Map<String, Object> getSuggestion(@RequestBody SpellingSuggestionRequestPOJO request) throws InterruptedException {
        return spellingTestService.getSuggestion(request, trieService);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/spelling/save")
    public Map<String, Object> saveData(@RequestBody RequestReportPOJO request) throws InterruptedException {
        return spellingTestService.saveSpellingTestResult(request);
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/api/spelling/loadData")
    public Map<String, Object> loadData(@RequestBody RequestCommonPOJO request) throws InterruptedException {
        return spellingTestService.loadPreviousSpellingTestResult(request);
    }


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
    @PostMapping("/api/brokenLink/getHistoryList")
    public Map<String, Object> getHistoryBrokenLinkList(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryBrokenLinkList(request);
    }

    @CrossOrigin
    @PostMapping("/api/brokenLink/getHistoryReport")
    public Map<String, Object> getHistoryBrokenLinkReport(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryBrokenLinkReport(request);
    }

    @CrossOrigin
    @PostMapping("/api/brokenLink/SaveReport")
    public Map<String, Object> saveBrokenLinkReport(@RequestBody RequestReportPOJO request) {
        return qualityService.saveBrokenLinkReport(request);
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
    @PostMapping("/api/brokenPage/getHistoryList")
    public Map<String, Object> getHistoryBrokenPageTestList(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryBrokenPageTestList(request);
    }

    @CrossOrigin
    @PostMapping("/api/brokenPage/getHistoryReport")
    public Map<String, Object> getHistoryBrokenPageTestReport(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryBrokenPageTestReport(request);
    }

    @CrossOrigin
    @PostMapping("/api/brokenPage/SaveReport")
    public Map<String, Object> saveBrokenPageReport(@RequestBody RequestReportPOJO request) {
        return qualityService.saveBrokenPageReport(request);
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

    @CrossOrigin
    @PostMapping("/api/prohibitedContent/getHistoryList")
    public Map<String, Object> getHistoryProhibitedTestList(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryProhibitedContentList(request);
    }

    @CrossOrigin
    @PostMapping("/api/prohibitedContent/getHistoryReport")
    public Map<String, Object> getHistoryProhibitedTestReport(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryProhibitedContentReport(request);
    }

    @CrossOrigin
    @PostMapping("/api/prohibitedContent/SaveReport")
    public Map<String, Object> saveProhibitedContentReport(@RequestBody RequestReportPOJO request) {
        return qualityService.saveProhibitedContentReport(request);
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

    @CrossOrigin
    @PostMapping("/api/missingtest/getHistoryList")
    public Map<String, Object> getHistoryMissingFileTestList(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryMissingFileTestList(request);
    }

    @CrossOrigin
    @PostMapping("/api/missingtest/getHistoryReport")
    public Map<String, Object> getHistoryMissingFileTestReport(@RequestBody RequestCommonPOJO request) {
        return qualityService.getHistoryMissingFileTestReport(request);
    }

    @PostMapping("/api/missingtest/saveReport")
    public Map<String, Object> saveMissingFileReport(@RequestBody RequestReportPOJO request) {
        return qualityService.saveMissingFileReport(request);
    }
}
