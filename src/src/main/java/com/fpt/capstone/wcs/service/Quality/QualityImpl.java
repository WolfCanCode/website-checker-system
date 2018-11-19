package com.fpt.capstone.wcs.service.Quality;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.service.Experience.ExperienceImpl;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
//chạy đc cái đã
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
    ProhibitedContentRepository prohibitedContentRepository;
    final
    WordRepository wordRepository;

    @Autowired
    public QualityImpl(Authenticate authenticate, PageOptionRepository pageOptionRepository, BrokenLinkRepository brokenLinkRepository, BrokenPageRepository brokenPageRepository, WordRepository wordRepository,ProhibitedContentRepository prohibitedContentRepository) {
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.brokenLinkRepository = brokenLinkRepository;
        this.brokenPageRepository = brokenPageRepository;
        this.wordRepository = wordRepository;
        this.prohibitedContentRepository = prohibitedContentRepository;

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

    public List<ProhibitedContentReport> prohibitedContentService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", Constant.CHROME_DRIVER);
        //Asign list JS info
        List<Word> wordList = new ArrayList<>();

        wordList = wordRepository.findAll();

            List<ProhibitedContentReport> resultList = new ArrayList<>();


        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        List<String> cookieNames = new ArrayList<String>();

        for (Page p : list) {
            List<Word> finalWordList = wordList;
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();

                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");

                        WebDriver driver = new ChromeDriver(chromeOptions);//chay an

                        driver.get(p.getUrl());
                        WebElement textt = driver.findElement(By.tagName("body"));
                        for (int i = 0; i< finalWordList.size(); i++){
                                if(textt.getText().toLowerCase().contains(finalWordList.get(i).getWord().toLowerCase())){
                                    ProhibitedContentReport prohibitedContentReport = new ProhibitedContentReport(p.getUrl(),finalWordList.get(i).getWord(),finalWordList.get(i).getType());
                                    prohibitedContentReport.setPageOption(option);
                                    resultList.add(prohibitedContentReport);
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
