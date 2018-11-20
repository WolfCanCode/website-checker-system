package com.fpt.capstone.wcs.service.Quality;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.MissingFilePOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.Experience.ExperienceImpl;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.atteo.evo.inflector.English;
import sun.misc.IOUtils;

@Service
public class QualityImpl implements QualityService {

    final
    Authenticate authenticate;
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
    WordRepository wordRepository;
    final
    InrregularVerbRepository inrregularVerbRepository;

    @Autowired
    public QualityImpl(Authenticate authenticate, PageOptionRepository pageOptionRepository, BrokenLinkRepository brokenLinkRepository, BrokenPageRepository brokenPageRepository, MissingFilesPagesRepository missingFilesPagesRepository, WordRepository wordRepository, ProhibitedContentRepository prohibitedContentRepository, InrregularVerbRepository inrregularVerbRepository) {
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.brokenLinkRepository = brokenLinkRepository;
        this.brokenPageRepository = brokenPageRepository;
        this.missingFilesPagesRepository = missingFilesPagesRepository;
        this.wordRepository = wordRepository;
        this.prohibitedContentRepository = prohibitedContentRepository;
        this.inrregularVerbRepository = inrregularVerbRepository;

    }





    @Override
    public Map<String, Object> getDataBrokenLink(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<BrokenLinkReport> resultList = brokenLinkService(pages, pageOption);
                brokenLinkRepository.removeAllByPageOption(pageOption);
                brokenLinkRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenLinkReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
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

                List<BrokenLinkReport> resultList = brokenLinkRepository.findAllByPageOption(pageOption);
                res.put("brokenLinkReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
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

    public List<BrokenLinkReport> brokenLinkService(List<Page> list, PageOption option) throws InterruptedException {
        //Asign list Broken Link
        List<BrokenLinkReport> resultList = new ArrayList<>();

        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();

        for (Page p : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        HttpURLConnection connection = (HttpURLConnection) new URL(p.getUrl()).openConnection();
                        connection.connect();
                        String responseMessage = connection.getResponseMessage();
                        int responseCode = connection.getResponseCode();
                        connection.disconnect();
                        System.out.println("URL: " + p.getUrl() + " returned " + responseMessage + " code " + responseCode);
                        if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                            BrokenLinkReport brokenLinkReport =  new BrokenLinkReport(responseCode, responseMessage,"https://www.nottingham.ac.uk/about", p.getUrl());
                            brokenLinkReport.setPageOption(option);
                            resultList.add(brokenLinkReport);



                        }

                        if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                            BrokenLinkReport brokenLinkReport =  new BrokenLinkReport(responseCode, responseMessage,"https://www.nottingham.ac.uk/about", p.getUrl());
                            brokenLinkReport.setPageOption(option);
                            resultList.add(brokenLinkReport);


                        }


                    }  catch (MalformedURLException e) {

                        System.out.println("hihi 1");
                        System.out.println("message 1" + e.getMessage());
                        if(e.toString().contains("java.net.MalformedURLException")){
                            BrokenLinkReport brokenLinkReport =  new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request","https://www.nottingham.ac.uk/about", p.getUrl());
                            brokenLinkReport.setPageOption(option);
                            resultList.add(brokenLinkReport);
                            //resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, ,"https://www.nottingham.ac.uk/about", p.getUrl()));
                        }
                        // Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("hihi 2");
                        System.out.println("message 2" + e.getMessage());
                        System.out.println("message 2.1" + e.toString());
                        if(e.toString().contains("java.net.UnknownHostException")){
                            BrokenLinkReport brokenLinkReport =  new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request","https://www.nottingham.ac.uk/about", p.getUrl());
                            brokenLinkReport.setPageOption(option);
                            resultList.add(brokenLinkReport);
                            //resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request","https://www.nottingham.ac.uk/about", p.getUrl()));
                        }

//                        if(e.toString().contains("java.net.ConnectException")){
//                            resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "Gateway Timeout","https://www.nottingham.ac.uk/about", u.getUrl()));
//                        }

                        //Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        System.out.println("hihi 3");
                        System.out.println("message 3" + e.getMessage());
                        //Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        System.out.println("hihi 4");
                        System.out.println("message 4" + e.getMessage());
                        // Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            t.join();
        }

        return resultList;

    }



    @Override
    public Map<String, Object> getDataBrokenPage(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<BrokenPageReport> resultList = brokenPageService(pages, pageOption);
                brokenPageRepository.removeAllByPageOption(pageOption);
                brokenPageRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("brokenPageReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
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

                List<BrokenPageReport> resultList = brokenPageRepository.findAllByPageOption(pageOption);
                res.put("brokenPageReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
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




    public List<BrokenPageReport> brokenPageService(List<Page> list, PageOption option) throws InterruptedException {
        //Asign list Broken Page
        List<BrokenPageReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();

        for (Page p : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
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
                            resultList.add(brokenPageReport);

                        }

                        for (int i = 0; i < errResponseCode.length; i++){
                            if(responseCode == errResponseCode[i] ){

                                BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", responseCode , responseMessage);
                                brokenPageReport.setPageOption(option);
                                resultList.add(brokenPageReport);

                            }

                        }




                    }  catch (MalformedURLException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                        if(e.toString().contains("java.net.MalformedURLException")){
                            BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request");
                            brokenPageReport.setPageOption(option);
                            resultList.add(brokenPageReport);
                            // resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request"));
                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                        if(e.toString().contains("java.net.UnknownHostException")){
                            BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request");
                            brokenPageReport.setPageOption(option);
                            resultList.add(brokenPageReport);
                            // resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request"));
                        }


                        if(e.toString().contains("java.net.ConnectException")){
                            BrokenPageReport brokenPageReport = new BrokenPageReport(p.getUrl(), "Error Page", HttpURLConnection.HTTP_GATEWAY_TIMEOUT , "Gateway Timeout");
                            brokenPageReport.setPageOption(option);
                            resultList.add(brokenPageReport);
                            //resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_GATEWAY_TIMEOUT , "Gateway Timeout"));
                        }
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            t.join();
        }

        return resultList;

    }

    @Override
    public Map<String, Object> getDataProhibitedContent(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<ProhibitedContentReport> resultList = prohibitedContentService(pages, pageOption);
                prohibitedContentRepository.removeAllByPageOption(pageOption);
                prohibitedContentRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("prohibitedContentReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
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

                List<ProhibitedContentReport> resultList = prohibitedContentRepository.findAllByPageOption(pageOption);
                res.put("prohibitedContentReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
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
    public Map<String, Object> getMissingFile(MissingFilePOJO request) {
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
                if(request.getListType().size()==0|| request.getListType().size()==4){
                    System.out.println("Vo ne");
                    List<MissingFileReport> resultList =  getMissingFile(pages,pageOption, urlRoot);
                    missingFilesPagesRepository.removeAllByPageOption(pageOption);
                    missingFilesPagesRepository.saveAll(resultList);
                    res.put("action", Constant.SUCCESS);
                    res.put("missingFileReport", resultList);
                }
                else{
                    for (Integer missingFilePOJODTO :request.getListType()){
                        switch (missingFilePOJODTO){
                            case 1:{
                                List<MissingFileReport> resultList=getMissingFileImg(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 2:{
                                List<MissingFileReport> resultList =getMissingFileCss(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 3:{
                                List<MissingFileReport> resultList =getMissingFileDoc(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;

                            }
                            case 4:{
                                List<MissingFileReport> resultList =getMissingFileARCHIVES(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
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
                if(request.getListType().size()==0|| request.getListType().size()==4){
                    System.out.println("Vo ne");
                    List<MissingFileReport> resultList =  getMissingFile(pages,pageOption, urlRoot);
                    missingFilesPagesRepository.removeAllByPageOption(pageOption);
                    missingFilesPagesRepository.saveAll(resultList);
                    res.put("action", Constant.SUCCESS);
                    res.put("missingFileReport", resultList);
                }
                else{
                    for (Integer missingFilePOJODTO :request.getListType()){
                        switch (missingFilePOJODTO){
                            case 1:{
                                List<MissingFileReport> resultList=getMissingFileImg(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 2:{
                                List<MissingFileReport> resultList =getMissingFileCss(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;
                            }
                            case 3:{
                                List<MissingFileReport> resultList =getMissingFileDoc(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
                                missingFilesPagesRepository.saveAll(resultList);
                                res.put("action", Constant.SUCCESS);
                                res.put("missingFileReport", resultList);
                                break;

                            }
                            case 4:{
                                List<MissingFileReport> resultList =getMissingFileARCHIVES(pages,pageOption, urlRoot);
                                missingFilesPagesRepository.removeAllByPageOption(pageOption);
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
                List<MissingFileReport> resultList = missingFilesPagesRepository.findAllByPageOption(pageOption);
                res.put("missingFileReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
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

    public List<MissingFileReport> getMissingFileImg(List<Page> list,PageOption option, String urlNew) {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (Page u : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(jpg|gif|jp2|jpeg|png|psd|tga|svg)", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            i++;
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("Image: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("Image Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                        System.out.println("Image Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("Image Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            System.out.println(urlRoot);

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return missing;
    }

    public List<MissingFileReport> getMissingFileDoc(List<Page> list,PageOption option, String urlNew)  {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for ( Page u : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(doc|docx|ppt|pptx|pdf|ps|txt|xls|xlsx)");
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("DOC: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("DOC Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                        System.out.println("DOC Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("DOC Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            System.out.println(urlRoot);

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return missing;
    }

    public List<MissingFileReport> getMissingFileCss(List<Page> list,PageOption option, String urlNew)  {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (Page u : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(css)(([\\?\\.\\=]\\w*)*)?", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
//            System.out.println(matcher.group());
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("CSS 2: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("CSS 2 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("CSS 2 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("CSS 2 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            System.out.println(urlRoot);

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return missing;
    }

    public List<MissingFileReport> getMissingFileMP3andMP4(List<Page> list,PageOption option, String urlNew) {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (Page u : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("(http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?(/\\S*)\\.(mp3|avi|flv|mp4)(\\w*)?", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                System.out.println("MP4: " + strChcek + " - Code:" + checkCode);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    System.out.println("MP4 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport( checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("MP4 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport( checkLast,  codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("MP4 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            System.out.println(urlRoot);

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return missing;
    }

    public List<MissingFileReport> getMissingFileARCHIVES(List<Page> list,PageOption option, String urlNew)  {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (Page u : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)(/\\S*)\\.(7z|zip|rar|jar|tar|tar|gz|cab)", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("ARCHIRE: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("ARCHIVES Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                        System.out.println("ARCHIVES Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("ARCHIVES Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            System.out.println(urlRoot);

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return missing;
    }

    public List<MissingFileReport> getMissingFile(List<Page> list,PageOption option, String urlNew) {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (Page u : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(jpg|gif|jp2|jpeg|pbm|pcx|pgm|png|ppm|psd|tiff|tga|svg)", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            i++;
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("Image: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("Image Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                        System.out.println("Image Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("Image Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                        //End check missing file


                        //Check missing doc
                        pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(doc|docx|djvu|odp|ods|odt|pps|ppsx|ppt|pptx|pdf|ps|eps|rtf|txt|wks|wps|xls|xlsx|xps)");
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("DOC: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("DOC Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                        System.out.println("DOC Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("DOC Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                        // end check missing doc

                        //check missing ARCHIVES
                        pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)(/\\S*)\\.(7z|zip|rar|jar|tar|tar|gz|cab)", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("ARCHIRE: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("ARCHIVES Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                        System.out.println("ARCHIVES Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("ARCHIVES Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                        //end check ARCHIVES

                        //check missing css
                        pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(css)(([\\?\\.\\=]\\w*)*)?", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
//            System.out.println(matcher.group());
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                                System.out.println("CSS 2: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                    System.out.println("CSS 2 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("CSS 2 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("CSS 2 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                        //end check missing css
                        // check missing file js

                        //end check missing file js
                        //check missing mp4 file
                        pattern = Pattern.compile("(http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?(/\\S*)\\.(3gp|avi|flv|m4v|mkv|mov|mp4|mpeg|ogv|wmv|webm)(\\w*)?", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                System.out.println("MP4: " + strChcek + " - Code:" + checkCode);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    System.out.println("MP4 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport( checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("MP4 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport( checkLast,  codeCheclast+" size: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("MP4 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                        //end check mp4 file

                        //check missing mp3 file
                        pattern = Pattern.compile("(http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?(/\\S*)\\.(aac|ac3|aiff|amr|ape|flac|m4a|mka|mp3|mpc|ogg|wav|wma)", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                System.out.println("MP3: " + strChcek + " - Code:" + checkCode);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size:: "+capacity.length, u.getUrl());
                                    fileNew.setPageOption(option);
                                    missing.add(fileNew);
                                }
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    System.out.println("MP3 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size:: "+capacity.length, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                    }
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size:: "+capacity.length, u.getUrl());
                                            fileNew.setPageOption(option);
                                            missing.add(fileNew);
                                        }
                                        System.out.println("MP3 Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        fileNew.setPageOption(option);
                                        missing.add(fileNew);
                                        System.out.println("MP3 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                        //end check Missing mp3 file
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            System.out.println(urlRoot);

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return missing;
    }


    // Function check http message Phúc Anh
    private String verifyHttpMessage(String url) {
        String message = "";
        try {
            URL urlTesst = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlTesst.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ");
            message = connection.getResponseMessage();
        } catch (Exception e) {
            message = "Not Found";
        }
        return message;
    }

    //Function get size file
    private  byte[] getBytes(String url)  {
        byte[] b = new byte[0];
        try {
            URL urlTesst = new URL(url);
            URLConnection uc = urlTesst.openConnection();
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

    public List<ProhibitedContentReport> prohibitedContentService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", Constant.CHROME_DRIVER);

        List<Word> wordList = new ArrayList<>();

        wordList = wordRepository.findAllByDelFlagEquals(false);
        System.out.println("wordlist " + wordList.size());

        List<InrregularVerb> inrregularVerbList = new ArrayList<>();

        inrregularVerbList = inrregularVerbRepository.findAll();
        List<ProhibitedContentReport> resultList = new ArrayList<>();


        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        List<Character> consonants = Arrays.asList('b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','x','y','z');// phu am
        List<Character> vowels = Arrays.asList('a', 'e', 'i', 'o', 'u');//nguyen am


        for (Page p : list) {
            List<Word> finalWordList = wordList;
            List<InrregularVerb> finalInrregularVerbList = inrregularVerbList;
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        String verb,verb1, verb2, verb3, verbing, verbed;

                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");
                        WebDriver driver = new ChromeDriver(chromeOptions);//chay an
                        driver.get(p.getUrl());
                        WebElement texts = driver.findElement(By.tagName("body"));
                        String  bodyText = texts.getText().toLowerCase();
                        for (int i = 0; i< finalWordList.size(); i++){
                            //String a = finalWordList.get(i).getWord().concat("," + );
                            String a = finalWordList.get(i).getWord().concat(",");
                            a = a.concat(English.plural(finalWordList.get(i).getWord()) + ",");

                            verb = finalWordList.get(i).getWord().toLowerCase();

                            if( verb.length()>1 && consonants.contains( verb.charAt(verb.length()-2)) && vowels.contains( verb.charAt(verb.length()-1))){
                                verbing = verb.substring(0, verb.length()-1) + "ing";
                                a = a.concat(verbing + ",");
                                //tan cung khong phai y va w, , truoc la nguyen am, truoc nua khong phai la nguyen am
                            }else{
                                verbing = verb + "ing";
                                a = a.concat(verbing + ",");
                            }

                            Boolean check = true;
                            for (int j = 0; j < finalInrregularVerbList.size(); j++ ){

                                    verb1 = finalInrregularVerbList.get(j).getVerbV1();
                                    verb2 = finalInrregularVerbList.get(j).getVerbV2();
                                    verb3 = finalInrregularVerbList.get(j).getVerbV3();

                                    if (verb.equalsIgnoreCase(verb1)){
                                        a = a.concat(verb2 + ",");
                                        a = a.concat(verb3 + ",");
                                        check = false;

                                    }
                                }

                                if(check == true){
                                    if(verb.charAt(verb.length()-1)=='e'){//tan cung e
                                        verbed = verb + "d";
                                        //tan cung y, truoc la phu am
                                    }else if(consonants.contains(verb.charAt(verb.length()-2)) && verb.charAt(verb.length()-1)=='y'){
                                        verbed = verb.substring(0, verb.length()-1) + "ied";
                                        //tan cung khong phai y va w, , truoc la nguyen am, truoc nua khong phai la nguyen am
                                    }else if(verb.length()>2 && !vowels.contains(verb.charAt(verb.length()-3)) && vowels.contains(verb.charAt(verb.length()-2)) && ( consonants.contains(verb.charAt(verb.length()-1)) && verb.charAt(verb.length()-1)!='w' && verb.charAt(verb.length()-1)!='y' )){
                                        verbed = verb + verb.charAt(verb.length()-1) + "ed";
                                    }else{
                                        verbed = verb + "ed";
                                    }
                                    a = a.concat(verbed + ",");

                                }





                            System.out.println("dsad" + a);
                            String[] list1 = a.split(",");
                            for(int j = 0; j < list1.length;j++){
                                if(bodyText.contains(list1[j].toLowerCase())){

                                    ProhibitedContentReport prohibitedContentReport = new ProhibitedContentReport(p.getUrl(),list1[j],finalWordList.get(i).getType());
                                    prohibitedContentReport.setPageOption(option);
                                    resultList.add(prohibitedContentReport);
                                }
                            }


                        }





                    }catch (InterruptedException | BrokenBarrierException e) {
                        Logger.getLogger(QualityImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            t.join();
        }




//
        return resultList;
    }


}
