package com.fpt.capstone.wcs.service.report.quality;

import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenPageReport;
import com.fpt.capstone.wcs.model.entity.report.quality.MissingFileReport;
import com.fpt.capstone.wcs.model.entity.report.quality.ProhibitedContentReport;
import com.fpt.capstone.wcs.model.entity.website.InrregularVerb;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.website.WarningWord;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;
import com.fpt.capstone.wcs.repository.report.quality.BrokenLinkRepository;
import com.fpt.capstone.wcs.repository.report.quality.BrokenPageRepository;
import com.fpt.capstone.wcs.repository.report.quality.MissingFilesPagesRepository;
import com.fpt.capstone.wcs.repository.report.quality.ProhibitedContentRepository;
import com.fpt.capstone.wcs.repository.website.InrregularVerbRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.repository.website.WarningWordRepository;
import com.fpt.capstone.wcs.service.report.experience.ExperienceImpl;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateService;
import com.fpt.capstone.wcs.utils.CheckHTTPResponse;
import com.fpt.capstone.wcs.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.atteo.evo.inflector.English;
import sun.misc.IOUtils;

@Service
public class QualityImpl implements QualityService {

    final
    AuthenticateService authenticate;
    final
    PageOptionRepository pageOptionRepository;
    final
    BrokenLinkRepository brokenLinkRepository;
    final
    BrokenPageRepository brokenPageRepository;
    final
    MissingFilesPagesRepository missingFilesPagesRepository;
    final
    ProhibitedContentRepository prohibitedContentRepository;
    final
    WarningWordRepository warningWordRepository;
    final
    InrregularVerbRepository inrregularVerbRepository;

    @Autowired
    public QualityImpl(AuthenticateService authenticate, PageOptionRepository pageOptionRepository, BrokenLinkRepository brokenLinkRepository, BrokenPageRepository brokenPageRepository, MissingFilesPagesRepository missingFilesPagesRepository, WarningWordRepository warningWordRepository, ProhibitedContentRepository prohibitedContentRepository, InrregularVerbRepository inrregularVerbRepository) {
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.brokenLinkRepository = brokenLinkRepository;
        this.brokenPageRepository = brokenPageRepository;
        this.missingFilesPagesRepository = missingFilesPagesRepository;
        this.warningWordRepository = warningWordRepository;
        this.prohibitedContentRepository = prohibitedContentRepository;
        this.inrregularVerbRepository = inrregularVerbRepository;

    }





    @Override
    public Map<String, Object> getDataBrokenLink(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(request);
        if (userWebsite != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), userWebsite.getWebsite(), false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                if (pages.size() == 0) {
                    Page page = new Page();
                    page.setUrl(userWebsite.getWebsite().getUrl());
                    page.setType(1);
                    pages.add(page);
                }
                List<BrokenLinkReport> resultList = brokenLinkService(pages, pageOption);
                brokenLinkRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenLinkReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(userWebsite.getWebsite().getUrl());
                page.setType(1);
                pages.add(page);
                List<BrokenLinkReport> resultList = brokenLinkService(pages, null);
                brokenLinkRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenLinkReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestBrokenLink(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }

            if(request.getPageOptionId()!=-1) {
                BrokenLinkReport brokenLinkReport = brokenLinkRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if(brokenLinkReport != null){
                    Date lastedCreatedTime = brokenLinkReport.getCreatedTime();
                    List<BrokenLinkReport> resultList = brokenLinkRepository.findAllByPageOptionAndCreatedTime(pageOption,lastedCreatedTime);
                    res.put("brokenLinkReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }else {
                    List<BrokenLinkReport> resultList = brokenLinkRepository.findAllByPageOptionAndUrlLink(null, website.getUrl());
                    res.put("brokenLinkReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }


            } else {
                List<BrokenLinkReport> resultList = brokenLinkRepository.findAllByPageOptionAndUrlLink(null,website.getUrl() );
                res.put("brokenLinkReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveBrokenLinkReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {

            List<BrokenLinkReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<BrokenLinkReport> optionalReport = brokenLinkRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    BrokenLinkReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<BrokenLinkReport> results = brokenLinkRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("brokenLinkReport", results);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    public List<BrokenLinkReport> brokenLinkService(List<Page> list, PageOption option) throws InterruptedException {
        //Asign list Broken Link
        List<BrokenLinkReport> resultList = new ArrayList<>();
        Date createdTime = new Date();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);

        for (Page p : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = p.getUrl();
                        int codeRespone = CheckHTTPResponse.verifyHttpMessage(url);
                        System.out.println("URL" + url);

                        System.out.println("codeRespone" + codeRespone);
                        if(codeRespone<400) {
                            String mainDomain = null;
                            if(url.contains("http://www.") || url.contains("https://www.") ){
                                mainDomain = url.split("www.")[1].split("/")[0];
                                System.out.println("main domain 1 : " + mainDomain);
                            }else if(url.contains("http://")){
                                mainDomain = url.split("http://")[1].split("/")[0];
                                System.out.println("main domain 2 : " + mainDomain);
                            }else if(url.contains("https://")){
                                mainDomain = url.split("https://")[1].split("/")[0];
                                System.out.println("main domain 3 : " + mainDomain);

                            }
                            String  type = null;
                            Document doc = Jsoup.connect(p.getUrl()).get();
                            Elements links = doc.select("a[href]");
                            Element link;

                            for (int j = 0; j < links.size(); j++) {
                                link = links.get(j);
                                //System.out.println("a= " + link.attr("abs:href"));
                                if(link.attr("abs:href") != null && link.attr("abs:href") != ""){
                                    if(link.attr("abs:href").contains(mainDomain)){
                                        type = "1";
                                    }else {
                                        type = "2";
                                    }
                                    try {
                                        HttpURLConnection connection = (HttpURLConnection) new URL(link.attr("abs:href")).openConnection();
                                        connection.connect();
                                        String responseMessage = connection.getResponseMessage();
                                        int responseCode = connection.getResponseCode();
                                        connection.disconnect();
                                        System.out.println("URL: " + link.attr("abs:href") + " returned " + responseMessage + " code " + responseCode);
                                        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                                            BrokenLinkReport brokenLinkReport = new BrokenLinkReport(responseCode, type,responseMessage, link.attr("abs:href"), p.getUrl());
                                            brokenLinkReport.setPageOption(option);
                                            brokenLinkReport.setCreatedTime(createdTime);
                                            resultList.add(brokenLinkReport);
                                        }

                                        if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                                            BrokenLinkReport brokenLinkReport = new BrokenLinkReport(responseCode,type, responseMessage, link.attr("abs:href"), p.getUrl());
                                            brokenLinkReport.setPageOption(option);
                                            brokenLinkReport.setCreatedTime(createdTime);
                                            resultList.add(brokenLinkReport);
                                        }
                                    } catch (MalformedURLException e) {
                                        System.out.println("message 1" + e.getMessage());
                                        if(e.toString().contains("java.net.MalformedURLException")){
                                            BrokenLinkReport brokenLinkReport =  new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, type,"Bad Request",link.attr("abs:href"), p.getUrl());
                                            brokenLinkReport.setPageOption(option);
                                            brokenLinkReport.setCreatedTime(createdTime);
                                            resultList.add(brokenLinkReport);
                                            //resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, ,"https://www.nottingham.ac.uk/about", p.getUrl()));
                                        }
                                        // Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                                    } catch (IOException e) {
                                        e.printStackTrace();

                                        System.out.println("message 2" + e.getMessage());
                                        System.out.println("message 2.1" + e.toString());
                                        if(e.toString().contains("java.net.UnknownHostException")){
                                            BrokenLinkReport brokenLinkReport =  new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST,type, "Bad Request",link.attr("abs:href"), p.getUrl());
                                            brokenLinkReport.setPageOption(option);
                                            brokenLinkReport.setCreatedTime(createdTime);
                                            resultList.add(brokenLinkReport);
                                            //resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request","https://www.nottingham.ac.uk/about", p.getUrl()));
                                        }
                                    }
                                }


                            }
                        }




                    }  catch (Exception e) {

                        System.out.println("message 3" + e.getMessage());
                        //Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        return resultList;

    }



    @Override
    public Map<String, Object> getDataBrokenPage(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(request);
        if (userWebsite != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), userWebsite.getWebsite(), false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                if (pages.size() == 0) {
                    Page page = new Page();
                    page.setUrl(userWebsite.getWebsite().getUrl());
                    page.setType(1);
                    pages.add(page);
                }
                List<BrokenPageReport> resultList = brokenPageService(pages, pageOption);
                brokenPageRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenPageReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(userWebsite.getWebsite().getUrl());
                page.setType(1);
                pages.add(page);
                List<BrokenPageReport> resultList = brokenPageService(pages, null);
                brokenPageRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenPageReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestBrokenPage(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }

            if(request.getPageOptionId()!=-1) {
                BrokenPageReport brokenPageReport = brokenPageRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if(brokenPageReport != null){
                    Date lastedCreatedTime = brokenPageReport.getCreatedTime();
                    List<BrokenPageReport> resultList = brokenPageRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("brokenPageReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }else {
                    List<BrokenPageReport> resultList = brokenPageRepository.findAllByPageOptionAndUrlPage(null, website.getUrl());
                    res.put("brokenPageReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }


            } else {
                List<BrokenPageReport> resultList = brokenPageRepository.findAllByPageOptionAndUrlPage(null, website.getUrl());
                res.put("brokenPageReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveBrokenPageReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<BrokenPageReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<BrokenPageReport> optionalReport = brokenPageRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    BrokenPageReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<BrokenPageReport> results = brokenPageRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("brokenPageReport", results);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }


    public List<BrokenPageReport> brokenPageService(List<Page> list, PageOption option) throws InterruptedException {
        //Asign list Broken Page
        Date createdTime = new Date();
        List<BrokenPageReport> resultList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        for (Page p : list) {
            executor.submit(new Runnable() {
                public void run() {
                    try {

                        HttpURLConnection connection = (HttpURLConnection) new URL(p.getUrl()).openConnection();
                        connection.connect();
                        String responseMessage = connection.getResponseMessage();
                        int responseCode = connection.getResponseCode();
                        connection.disconnect();
                        int[] errResponseCode = {203 ,204 , 205 , 206, 403 ,400,  405 , 409 , 500 , 511, 503 , 505, 507, 510 , 511 };

                        System.out.println("URL: " + p.getUrl() + " returned " + responseMessage + " code " + responseCode);
                        if(responseCode == 404){

                            // resultList.add(new BrokenPageReport(p.getUrl(), "Missing Page", responseCode , responseMessage));
                            BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Missing Page", responseCode , responseMessage);
                            brokenPageReport.setPageOption(option);
                            brokenPageReport.setCreatedTime(createdTime);
                            resultList.add(brokenPageReport);

                        }

                        for (int i = 0; i < errResponseCode.length; i++){
                            if(responseCode == errResponseCode[i] ){

                                BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", responseCode , responseMessage);
                                brokenPageReport.setPageOption(option);
                                brokenPageReport.setCreatedTime(createdTime);
                                resultList.add(brokenPageReport);

                            }

                        }

                    }  catch (MalformedURLException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                        if(e.toString().contains("java.net.MalformedURLException")){
                            BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request");
                            brokenPageReport.setPageOption(option);
                            brokenPageReport.setCreatedTime(createdTime);
                            resultList.add(brokenPageReport);
                            // resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request"));
                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                        if(e.toString().contains("java.net.UnknownHostException")){
                            BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request");
                            brokenPageReport.setPageOption(option);
                            brokenPageReport.setCreatedTime(createdTime);
                            resultList.add(brokenPageReport);
                            // resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request"));
                        }


                        if(e.toString().contains("java.net.ConnectException")){
                            BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", HttpURLConnection.HTTP_GATEWAY_TIMEOUT , "Gateway Timeout");
                            brokenPageReport.setPageOption(option);
                            brokenPageReport.setCreatedTime(createdTime);
                            resultList.add(brokenPageReport);
                            //resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_GATEWAY_TIMEOUT , "Gateway Timeout"));
                        }
                    }
                        catch (Exception e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);

        return resultList;

    }

    @Override
    public Map<String, Object> getDataProhibitedContent(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(request);

        if (userWebsite != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), userWebsite.getWebsite(), false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                if (pages.size() == 0) {
                    Page page = new Page();
                    page.setUrl(userWebsite.getWebsite().getUrl());
                    page.setType(1);
                    pages.add(page);
                }
                List<ProhibitedContentReport> resultList = prohibitedContentService(pages, pageOption);
                prohibitedContentRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("prohibitedContentReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(userWebsite.getWebsite().getUrl());
                page.setType(1);
                pages.add(page);
                List<ProhibitedContentReport> resultList = prohibitedContentService(pages, null);
                prohibitedContentRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("prohibitedContentReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }

    }

    @Override
    public Map<String, Object> getLastestProhibitedContent(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }

            if(request.getPageOptionId()!=-1) {
                ProhibitedContentReport prohibitedContentReport = prohibitedContentRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if(prohibitedContentReport != null){
                    Date lastedCreatedTime = prohibitedContentReport.getCreatedTime();
                    List<ProhibitedContentReport> resultList = prohibitedContentRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("prohibitedContentReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }else {
                    List<ProhibitedContentReport> resultList = prohibitedContentRepository.findAllByPageOptionAndUrlPage(null, website.getUrl());
                    res.put("prohibitedContentReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }


            } else {
                List<ProhibitedContentReport> resultList = prohibitedContentRepository.findAllByPageOptionAndUrlPage(null,website.getUrl() );
                res.put("prohibitedContentReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveProhibitedContentReport(RequestReportPOJO request) {

        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<ProhibitedContentReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<ProhibitedContentReport> optionalReport = prohibitedContentRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    ProhibitedContentReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<ProhibitedContentReport> results = prohibitedContentRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("prohibitedContentReport", results);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }


    @Override
    public Map<String, Object> getMissingFile(MissingFilePOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO pojo = new RequestCommonPOJO();
        pojo.setPageOptionId(request.getPageOptionId());
        pojo.setUserId(request.getUserId());
        pojo.setUserToken(request.getUserToken());
        pojo.setWebsiteId(request.getWebsiteId());
        Website website =authenticate.isAuthGetSingleSite(pojo);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                String urlRoot="";
                for(int i =0; i< pages.size();i++ ){
                    Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(pages.get(i).getUrl());
                    while (matcher.find()){
                        urlRoot = matcher.group();
                    }
                }
                int codeCheckRedirection = CheckHTTPResponse.verifyHttpMessage(urlRoot);
                if(codeCheckRedirection>=300&&codeCheckRedirection<=400){
                    try {
                        urlRoot = CheckHTTPResponse.getURLDirectsTo(urlRoot);
                        System.out.println("Check Redirection url Roort "+urlRoot);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(request.getListType().size()==0|| request.getListType().size()==4){
                    System.out.println("Vo ne");
                    List<MissingFileReport> resultList =  getMissingFile(pages,pageOption, urlRoot);

                    missingFilesPagesRepository.saveAll(resultList);
                    res.put("action", Constant.SUCCESS);
                    res.put("missingFileReport", resultList);
                }
                else{
                    for (Integer missingFilePOJODTO :request.getListType()){
                        switch (missingFilePOJODTO){
                            case 1:{
                                List<MissingFileReport> resultList=getMissingFileImg(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 2:{
                                List<MissingFileReport> resultList =getMissingFileCss(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 3:{
                                List<MissingFileReport> resultList =getMissingFileDoc(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;

                            }
                            case 4:{
                                List<MissingFileReport> resultList =getMissingFileARCHIVES(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                        }
                    }
                }
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                String urlRoot="";
                for(int i =0; i< pages.size();i++ ){
                    Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(pages.get(i).getUrl());
                    while (matcher.find()){
                        urlRoot = matcher.group();
                    }
                }
                int codeCheckRedirection = CheckHTTPResponse.verifyHttpMessage(urlRoot);
                if(codeCheckRedirection>=300&&codeCheckRedirection<=400){
                    try {
                        urlRoot = CheckHTTPResponse.getURLDirectsTo(urlRoot);
                        System.out.println("Check Redirection url Roort "+urlRoot);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(request.getListType().size()==0|| request.getListType().size()==4){
                    System.out.println("Vo ne");
                    List<MissingFileReport> resultList =  getMissingFile(pages,pageOption, urlRoot);

                    missingFilesPagesRepository.saveAll(resultList);
                    res.put("action", Constant.SUCCESS);
                    res.put("missingFileReport", resultList);
                }
                else{
                    for (Integer missingFilePOJODTO :request.getListType()){
                        switch (missingFilePOJODTO){
                            case 1:{
                                List<MissingFileReport> resultList=getMissingFileImg(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 2:{
                                List<MissingFileReport> resultList =getMissingFileCss(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 3:{
                                List<MissingFileReport> resultList =getMissingFileDoc(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;

                            }
                            case 4:{
                                List<MissingFileReport> resultList =getMissingFileARCHIVES(pages,pageOption, urlRoot);

                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                        }
                    }
                }
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestMissingFile(MissingFilePOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO pojo = new RequestCommonPOJO();
        pojo.setPageOptionId(request.getPageOptionId());
        pojo.setUserId(request.getUserId());
        pojo.setUserToken(request.getUserToken());
        pojo.setWebsiteId(request.getWebsiteId());
        Website website = authenticate.isAuthGetSingleSite(pojo);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                MissingFileReport contactReport = missingFilesPagesRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (contactReport != null) {
                    Date lastedCreatedTime = contactReport.getCreatedTime();
                    List<MissingFileReport> resultList =  missingFilesPagesRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("missingFileReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                } else {
                    res.put("missingFileReport", new ArrayList<>());
                    res.put("action", Constant.SUCCESS);
                    return res;
                }
            } else {
                List<MissingFileReport> resultList = missingFilesPagesRepository.findAllByPageOptionAndPages(null, website.getUrl());
                res.put("missingFileReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveMissingFileReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<MissingFileReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<MissingFileReport> optionalReport =  missingFilesPagesRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    MissingFileReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<MissingFileReport> results = missingFilesPagesRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("missingFileReport", results);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    public List<MissingFileReport> getMissingFileImg(List<Page> list,PageOption option, String urlNew) throws InterruptedException {
        Date createdTime = new Date();
        List<MissingFileReport> missing = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        String urlRoot = urlNew;
        for (Page u : list) {
            executor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                            List<MissingFileReport> missingFileImage  =  checkMissingFile(Constant.PATTERN_IMAGE, urlRoot, createdTime, u, option);
                                            missing.addAll(missingFileImage);

                                    } catch (IOException e) {
                                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                                    }
                                }

                            });
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);
        return missing;
    }


    public List<MissingFileReport> getMissingFileDoc(List<Page> list,PageOption option, String urlNew) throws InterruptedException {
        Date createdTime = new Date();
        List<MissingFileReport> missing = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        String urlRoot = urlNew;
        for ( Page u : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                            List<MissingFileReport> missingFileDOC  =  checkMissingFile(Constant.PATTERN_DOC, urlRoot, createdTime, u, option);
                            missing.addAll(missingFileDOC);
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }});

            System.out.println(urlRoot);
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);

        return missing;
    }


    public List<MissingFileReport> getMissingFileCss(List<Page> list,PageOption option, String urlNew) throws InterruptedException {
        Date createdTime = new Date();
        List<MissingFileReport> missing = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        String urlRoot = urlNew;
        for (Page u : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<MissingFileReport> missingFileCSS  =  checkMissingFile(Constant.PATTERN_CSS, urlRoot, createdTime, u, option);
                        missing.addAll(missingFileCSS);

                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });

            System.out.println(urlRoot);

        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);

        return missing;
    }

    public List<MissingFileReport> getMissingFileMP3andMP4(List<Page> list,PageOption option, String urlNew) throws InterruptedException {

        Date createdTime = new Date();
        List<MissingFileReport> missing = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        String urlRoot = urlNew;
        for ( Page u : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<MissingFileReport> missingFileImage  =  checkMissingFile(Constant.PATTERN_MP3_PM4, urlRoot, createdTime, u, option);
                        missing.addAll(missingFileImage);

                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }});

            System.out.println(urlRoot);
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);

        return missing;
    }

    public List<MissingFileReport> getMissingFileARCHIVES(List<Page> list,PageOption option, String urlNew)  {
        Date createdTime = new Date();
        List<MissingFileReport> missing = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        String urlRoot = urlNew;
        for (Page u : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        List<MissingFileReport> missingFileARCHIVES  =  checkMissingFile(Constant.PATTERN_ACRHIVES, urlRoot, createdTime, u, option);
                        missing.addAll(missingFileARCHIVES);
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }});

            System.out.println(urlRoot);

        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return missing;
    }

    public List<MissingFileReport> getMissingFile(List<Page> list,PageOption option, String urlNew) throws InterruptedException {
        Date createdTime = new Date();
        List<MissingFileReport> missing = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        String urlRoot = urlNew;
        for (Page u : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        List<MissingFileReport> missingFileImage  =  checkMissingFile(Constant.PATTERN_IMAGE, urlRoot, createdTime, u, option);
                        missing.addAll(missingFileImage);
                        List<MissingFileReport> missingFileARCHIVES  =  checkMissingFile(Constant.PATTERN_ACRHIVES, urlRoot, createdTime, u, option);
                        missing.addAll( missingFileARCHIVES);
                        List<MissingFileReport> missingFileCSS  =  checkMissingFile(Constant.PATTERN_CSS, urlRoot, createdTime, u, option);
                        missing.addAll(missingFileCSS);
                        List<MissingFileReport> missingFileDOC  =  checkMissingFile(Constant.PATTERN_DOC, urlRoot, createdTime, u, option);
                        missing.addAll(missingFileDOC);
                        List<MissingFileReport> missingFileMP3MP4  =  checkMissingFile(Constant.PATTERN_MP3_PM4, urlRoot, createdTime, u, option);
                        missing.addAll(missingFileMP3MP4);
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }});
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);

        return missing;
    }


    //Function get size file
    private  byte[] getBytes(String url)  {
        byte[] b = new byte[0];
        try {
            URL urlTesst = new URL(url);
            HttpURLConnection uc =(HttpURLConnection) urlTesst.openConnection();
            uc.setRequestMethod("GET");
            uc.setRequestProperty("User-Agent","Mozilla/5.0 ");
            int len = uc.getContentLength();
            InputStream in = new BufferedInputStream(uc.getInputStream());

            try {
                b = IOUtils.readFully(in, len, true);
            } finally {
                in.close();
            }
        }catch (IOException ex){

        }

        return b;
    }


    private List<MissingFileReport> checkMissingFile(String patternForma, String urlRoot, Date createdTime, Page u, PageOption option) throws IOException {
        Document doc = Jsoup.connect(u.getUrl()).get();
        List<MissingFileReport> missing = new ArrayList<>();
        Elements elemBase = doc.select("base");
        boolean flagBase = false;
        String baseHref = "";
        for (Element element : elemBase) {
            System.out.println(element);
            baseHref = element.attr("href");
            System.out.println();
            flagBase = true;
        }
        //check redirect to external
        boolean flagExternal =false;

                                        int code = CheckHTTPResponse.verifyHttpMessage(u.getUrl());
                                        if(code>=300&& code<400){
                                            String urlRedirect = CheckHTTPResponse.getURLDirectsTo(u.getUrl());
                                            Pattern pattern = Pattern.compile("(http\\:|https\\:)//(([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)",Pattern.CASE_INSENSITIVE);
                                            Matcher matcher = pattern.matcher(urlRedirect);
                                            String urlNew ="";
                                            while (matcher.find()){
                                              urlNew = matcher.group(2);
                                            }

                                            String rootURlOrignial ="";
                                            matcher = pattern.matcher(urlRoot);
                                            while (matcher.find()){
                                                rootURlOrignial = matcher.group(2);
                                            }

                                            if(!rootURlOrignial.equals(urlNew)){
                                                flagExternal =true;
                                            }

                                        }

                                        if(flagExternal==true){
                                            MissingFileReport external = new MissingFileReport("", "Redirect to External Link", u.getUrl());
                                            missing.add(external);
                                            flagExternal=false;
                                        }
                                        //start check missing file
                                        else {
                                            Pattern pattern = Pattern.compile(patternForma, Pattern.CASE_INSENSITIVE);
                                            Matcher matcher = pattern.matcher(doc.html());
                                            while (matcher.find()) {
                                                String strChcek = matcher.group();
                                                if (!strChcek.startsWith("//$")) {
                                                    int checkCode = CheckHTTPResponse.verifyHttpMessage(strChcek);

                                                    //check
                                                    if (checkCode < 400) {
                                                        byte[] capacity = getBytes(strChcek);
                                                        System.out.println();
                                                        if (capacity.length == 0) {
                                                            MissingFileReport fileNew = new MissingFileReport(strChcek, " size: " + capacity.length, u.getUrl());
                                                            fileNew.setPageOption(option);
                                                            fileNew.setCreatedTime(createdTime);
                                                            missing.add(fileNew);
                                                        }
                                                    }
                                                    if (400 <= checkCode) {
                                                        String checkAgain = urlRoot + strChcek;
                                                        String newStCheck = "";
                                                        if(strChcek.startsWith("../")){
                                                            newStCheck = strChcek.replaceFirst("../","/");
                                                            System.out.println(newStCheck);
                                                            checkAgain= urlRoot+ newStCheck;
                                                        }
                                                        int codeCheckAgain = CheckHTTPResponse.verifyHttpMessage(checkAgain);
                                                        if (codeCheckAgain < 400) {
                                                            if (codeCheckAgain >= 300 && codeCheckAgain < 400) {
                                                                String newUrl = CheckHTTPResponse.getURLDirectsTo(checkAgain);
                                                                urlRoot = newUrl;
                                                            }
                                                            byte[] capacity = getBytes(checkAgain);
                                                            if (capacity.length == 0) {
                                                                MissingFileReport fileNew = new MissingFileReport(checkAgain, " size: " + capacity.length, u.getUrl());
                                                                fileNew.setPageOption(option);
                                                                fileNew.setCreatedTime(createdTime);
                                                                missing.add(fileNew);
                                                            }
                                                        }
                                                        if (400 <= codeCheckAgain) {

                                                            String checkLast = "https:" + strChcek;
                                                            String newStCheckLast = "";
                                                            if(strChcek.startsWith("../")){
                                                                newStCheckLast = strChcek.replaceFirst("../","/");
                                                                checkLast= "https:"+ newStCheckLast;
                                                            }
                                                            int codeCheclast = CheckHTTPResponse.verifyHttpMessage(checkLast);
                                                            if (codeCheclast < 400) {
                                                                byte[] capacity = getBytes(checkLast);
                                                                if (capacity.length == 0) {
                                                                    MissingFileReport fileNew = new MissingFileReport(checkLast, " size: " + capacity.length, u.getUrl());
                                                                    fileNew.setPageOption(option);
                                                                    fileNew.setCreatedTime(createdTime);
                                                                    missing.add(fileNew);
                                                                }
                                                            } else {
                                                                if (flagBase == true) {
                                                                    if (strChcek.startsWith("../")) {
                                                                        List<String> partoFBase = new ArrayList<>();
                                                                        System.out.println(baseHref.replaceFirst("", ""));
                                                                        ;
                                                                        StringTokenizer strToken = new StringTokenizer(baseHref, "/");
                                                                        while (strToken.hasMoreTokens()) {
                                                                            partoFBase.add(strToken.nextToken());
                                                                        }
                                                                        String newBaseurl = baseHref.replaceFirst(partoFBase.get(partoFBase.size() - 1), "");
                                                                        String newStrcheck = strChcek.replaceFirst("../", "");
                                                                        String checkLastBase = newBaseurl + newStrcheck;
                                                                        int codeCheckLastBase = CheckHTTPResponse.verifyHttpMessage(checkLastBase);
                                                                        if (codeCheckLastBase < 400) {
                                                                            byte[] weBase = getBytes(checkLastBase);
                                                                            if (weBase.length == 0) {
                                                                                MissingFileReport fileNew = new MissingFileReport(strChcek, "size " + weBase.length, u.getUrl());
                                                                                fileNew.setPageOption(option);
                                                                                fileNew.setCreatedTime(createdTime);
                                                                                missing.add(fileNew);

                                                                            }



                                                                        } else {
                                                                            MissingFileReport fileNew = new MissingFileReport(strChcek, "" + codeCheckLastBase, u.getUrl());
                                                                            fileNew.setPageOption(option);
                                                                            fileNew.setCreatedTime(createdTime);
                                                                            missing.add(fileNew);
                                                                        }

                                                                    } else {
                                                                        String checkBaseLast = baseHref + strChcek;
                                                                        int codeCheckLastBase = CheckHTTPResponse.verifyHttpMessage(checkBaseLast);
                                                                        if (codeCheckLastBase < 400) {
                                                                            byte[] weBase = getBytes(checkBaseLast);
                                                                            if (weBase.length == 0) {
                                                                                MissingFileReport fileNew = new MissingFileReport(strChcek, "size: " + weBase.length, u.getUrl());
                                                                                fileNew.setPageOption(option);
                                                                                fileNew.setCreatedTime(createdTime);
                                                                                missing.add(fileNew);
                                                                            }

                                                                        } else {

                                                                            MissingFileReport fileNew = new MissingFileReport(strChcek, "" + codeCheckLastBase, u.getUrl());
                                                                            fileNew.setPageOption(option);
                                                                            fileNew.setCreatedTime(createdTime);
                                                                            missing.add(fileNew);
                                                                        }
                                                                    }
                                                                } else {
                                                                    MissingFileReport fileNew = new MissingFileReport(strChcek, "" + codeCheclast, u.getUrl());
                                                                    fileNew.setPageOption(option);
                                                                    fileNew.setCreatedTime(createdTime);
                                                                    missing.add(fileNew);
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }

        return  missing;
    }
    public List<ProhibitedContentReport> prohibitedContentService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", Constant.CHROME_DRIVER);
        Date createdTime = new Date();
        List<WarningWord> wordList = new ArrayList<>();

        wordList = warningWordRepository.findAllByDelFlagEquals(false);
        System.out.println("wordlist " + wordList.size());
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        List<InrregularVerb> inrregularVerbList = new ArrayList<>();

        inrregularVerbList = inrregularVerbRepository.findAll();
        List<ProhibitedContentReport> resultList = new ArrayList<>();
        List<Character> consonants = Arrays.asList('b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','x','y','z');// phu am
        List<Character> vowels = Arrays.asList('a', 'e', 'i', 'o', 'u');//nguyen am

        for (Page p : list) {
            List<WarningWord> finalWordList = wordList;
            List<InrregularVerb> finalInrregularVerbList = inrregularVerbList;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = p.getUrl();
                        int codeRespone = CheckHTTPResponse.verifyHttpMessage(url);
                        System.out.println("URL" + url);

                        System.out.println("codeRespone" + codeRespone);
                        if(codeRespone<400) {

                            String verb, verb1, verb2, verb3, verbing, verbed;

                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.addArguments("--headless");
                            WebDriver driver = new ChromeDriver(chromeOptions);//chay an
                            driver.get(p.getUrl());
                            WebElement texts = driver.findElement(By.tagName("body"));
                            String bodyTextWithSpace = texts.getText().toLowerCase();
                            String bodyText = bodyTextWithSpace.replaceAll("\\s+", " ");
                            for (int i = 0; i < finalWordList.size(); i++) {
                                //String a = finalWordList.get(i).getWord().concat("," + );
                                String a = finalWordList.get(i).getWord().concat(",");
                                a = a.concat(English.plural(finalWordList.get(i).getWord()) + ",");

                                verb = finalWordList.get(i).getWord().toLowerCase();

                                if (verb.length() > 1 && consonants.contains(verb.charAt(verb.length() - 2)) && vowels.contains(verb.charAt(verb.length() - 1))) {
                                    verbing = verb.substring(0, verb.length() - 1) + Constant.Verb_type[3];
                                    a = a.concat(verbing + ",");
                                    //tan cung khong phai y va w, , truoc la nguyen am, truoc nua khong phai la nguyen am
                                } else {
                                    verbing = verb + Constant.Verb_type[3];
                                    a = a.concat(verbing + ",");
                                }

                                Boolean check = true;
                                for (int j = 0; j < finalInrregularVerbList.size(); j++) {

                                    verb1 = finalInrregularVerbList.get(j).getVerbV1();
                                    verb2 = finalInrregularVerbList.get(j).getVerbV2();
                                    verb3 = finalInrregularVerbList.get(j).getVerbV3();

                                    if (verb.equalsIgnoreCase(verb1)) {
                                        a = a.concat(verb2 + ",");
                                        a = a.concat(verb3 + ",");
                                        check = false;

                                    }
                                }

                                if (check == true) {
                                    if (verb.charAt(verb.length() - 1) == 'e') {//tan cung e
                                        verbed = verb + Constant.Verb_type[0];
                                        //tan cung y, truoc la phu am
                                    } else if (consonants.contains(verb.charAt(verb.length() - 2)) && verb.charAt(verb.length() - 1) == 'y') {
                                        verbed = verb.substring(0, verb.length() - 1) + Constant.Verb_type[2];
                                        //tan cung khong phai y va w, , truoc la nguyen am, truoc nua khong phai la nguyen am
                                    } else if (verb.length() > 2 && !vowels.contains(verb.charAt(verb.length() - 3)) && vowels.contains(verb.charAt(verb.length() - 2)) && (consonants.contains(verb.charAt(verb.length() - 1)) && verb.charAt(verb.length() - 1) != 'w' && verb.charAt(verb.length() - 1) != 'y')) {
                                        verbed = verb + verb.charAt(verb.length() - 1) + Constant.Verb_type[1];
                                    } else {
                                        verbed = verb + Constant.Verb_type[1];
                                    }
                                    a = a.concat(verbed + ",");

                                }


                                System.out.println("dsad" + a);
                                String[] list1 = a.split(",");
                                for (int j = 0; j < list1.length; j++) {

                                    int index = bodyText.indexOf(list1[j].toLowerCase());
                                    while (index >= 0) {

                                        String fragment = bodyText.substring(index, index + 30);

                                        ProhibitedContentReport prohibitedContentReport = new ProhibitedContentReport(p.getUrl(), list1[j], fragment, finalWordList.get(i).getTopic().getTypeName());
                                        prohibitedContentReport.setPageOption(option);
                                        prohibitedContentReport.setCreatedTime(createdTime);
                                        resultList.add(prohibitedContentReport);
                                        index = bodyText.indexOf(list1[j].toLowerCase(), index + 1);
                                    }
                                }


                            }


                        }


                    }catch (Exception e) {
                        Logger.getLogger(QualityImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }





        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);
        return resultList;
    }


}
