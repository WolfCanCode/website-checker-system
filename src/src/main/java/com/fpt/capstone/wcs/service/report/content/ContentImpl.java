package com.fpt.capstone.wcs.service.report.content;

import com.fpt.capstone.wcs.model.entity.report.experience.MobileLayoutReport;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.report.content.ContactReport;
import com.fpt.capstone.wcs.model.entity.report.content.PageReport;
import com.fpt.capstone.wcs.model.entity.report.content.RedirectionReport;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;
import com.fpt.capstone.wcs.repository.report.content.ContactDetailRepository;
import com.fpt.capstone.wcs.repository.report.content.LinkRedirectionRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.repository.report.content.PageTestRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateService;
import com.fpt.capstone.wcs.utils.CheckHTTPResponse;
import com.fpt.capstone.wcs.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContentImpl implements  ContentService {

    final
    AuthenticateService authenticate;
    final
    PageOptionRepository pageOptionRepository;
    final ContactDetailRepository contactDetailRepository;
    final PageTestRepository pageTestRepository;
    final LinkRedirectionRepository linkRedirectionRepository;
    @Autowired
    public ContentImpl(AuthenticateService authenticate, PageOptionRepository pageOptionRepository, ContactDetailRepository contactDetailRepository, PageTestRepository pageTestRepository, LinkRedirectionRepository linkRedirectionRepository) {
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.pageTestRepository = pageTestRepository;
        this.linkRedirectionRepository = linkRedirectionRepository;
    }

    @Override
    public Map<String, Object> getDataPagesTest(RequestCommonPOJO request) throws InterruptedException {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();

                List<PageReport> resultList = getPageInfor(pages, pageOption);
//                pageTestRepository.removeAllByPageOption(pageOption);
                pageTestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("pagetestReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);

                List<PageReport> resultList = getPageInfor(pages, null);
                pageTestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("pagetestReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestPageTest(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                PageReport pageTestReport = pageTestRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (pageTestReport != null) {
                    Date lastedCreatedTime = pageTestReport.getCreatedTime();
                    List<PageReport> resultList = pageTestRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("pagetestReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                } else {
                    res.put("pagetestReport", new ArrayList<>());
                    res.put("action", Constant.SUCCESS);
                    return res;
                }
            } else {
                List<PageReport> resultList = pageTestRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("pagetestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> savePageReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<PageReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<PageReport> optionalReport =  pageTestRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                     PageReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<PageReport> results = pageTestRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("pagetestReport", results);
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
    public Map<String, Object> getHistoryPagesTestReport(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Date time = new Date(request.getReportTime());
        List<PageReport> pageReports = pageTestRepository.findALlByCreatedTime(time);
        res.put("action",Constant.SUCCESS);
        res.put("data",pageReports);
        return res;
    }

    @Override
    public Map<String, Object> getHistoryPagesTestList(RequestCommonPOJO request) {
        List<PageReport> list = pageTestRepository.findAllGroupByCreatedTimeAndPageOption(request.getUserId(), request.getPageOptionId());
        Map<String, Object> res = new HashMap<>();
        List<Long> dates = new ArrayList<>();
        for(int i = 0 ; i< list.size();i++)
        {
            dates.add(list.get(i).getCreatedTime().getTime());
        }
        res.put("action",Constant.SUCCESS);
        res.put("data",dates);
        return res;
    }

    @Override
    public Map<String, Object> getDataRedirectTest(RequestCommonPOJO request) throws InterruptedException {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();

                List<RedirectionReport> resultList = null;

                resultList = redirectionTest(pages, pageOption);

                linkRedirectionRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("redirectiontestReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);

                List<RedirectionReport> resultList = redirectionTest(pages, null);
                linkRedirectionRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("redirectiontestReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestLinkRedirection(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                RedirectionReport redirectionReport = linkRedirectionRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (redirectionReport != null) {
                    Date lastedCreatedTime = redirectionReport.getCreatedTime();
                    List<RedirectionReport> resultList = linkRedirectionRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("redirectiontestReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                } else {
                    res.put("redirectiontestReport", new ArrayList<>());
                    res.put("action", Constant.SUCCESS);
                    return res;
                }
            } else {
                List<RedirectionReport> resultList = linkRedirectionRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("redirectiontestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveRedirectionReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<RedirectionReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional< RedirectionReport> optionalReport =  linkRedirectionRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    RedirectionReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<RedirectionReport> results = linkRedirectionRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("redirectiontestReport", results);
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
    public Map<String, Object> getDataContactDetail(RequestCommonPOJO request) throws InterruptedException {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<ContactReport> resultList = null;
                resultList = getContactDetail(pages, pageOption);
                contactDetailRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("contactReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);

                List<ContactReport> resultList = getContactDetail(pages, null);
                contactDetailRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("contactReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestContactDetail(RequestCommonPOJO request) {

        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                ContactReport contactReport = contactDetailRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (contactReport != null) {
                    Date lastedCreatedTime = contactReport.getCreatedTime();
                    List<ContactReport> resultList = contactDetailRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("contactReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                } else {
                    res.put("contactReport", new ArrayList<>());
                    res.put("action", Constant.SUCCESS);
                    return res;
                }
            } else {
                List<ContactReport> resultList = contactDetailRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("contactReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveContactDetailReport(RequestReportPOJO request) {

        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<ContactReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<ContactReport> optionalReport =  contactDetailRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    ContactReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<ContactReport> results = contactDetailRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("contactReport", results);
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
    public Map<String, Object> getHistoryContactDetailTestReport(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Date time = new Date(request.getReportTime());
        List<ContactReport> contactReports = contactDetailRepository.findALlByCreatedTime(time);
        res.put("action",Constant.SUCCESS);
        res.put("data",contactReports);
        return res;
    }

    @Override
    public Map<String, Object> getHistoryContactDetailTestList(RequestCommonPOJO request) {
        List<ContactReport> list = contactDetailRepository.findAllGroupByCreatedTimeAndPageOption(request.getUserId(), request.getPageOptionId());
        Map<String, Object> res = new HashMap<>();
        List<Long> dates = new ArrayList<>();
        for(int i = 0 ; i< list.size();i++)
        {
            dates.add(list.get(i).getCreatedTime().getTime());
        }
        res.put("action",Constant.SUCCESS);
        res.put("data",dates);
        return res;
    }


    public List<PageReport> getPageInfor(List<Page> list, PageOption option) throws InterruptedException {
        Date createdTime = new Date();
        List<PageReport> pageCheck = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        for (Page url:list){
            executor.submit(new Runnable() {
                @Override
                public void run() {
                        String title = getTitle(url.getUrl());
                        int  httpcode = CheckHTTPResponse.verifyHttpMessage(url.getUrl());
                        System.out.println("HTTP  code"+ CheckHTTPResponse.verifyHttpMessage(url.getUrl()));
                        String canoUrl = getCanonicalUrl(url.getUrl());
                        PageReport page = new PageReport(httpcode, url.getUrl(),title,canoUrl);
                        page.setPageOption(option);
                        page.setCreatedTime(createdTime);
                        pageCheck.add(page);
                }});
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        return pageCheck;
    }

    public  List<RedirectionReport> redirectionTest(List<Page> list, PageOption option) throws InterruptedException {
        Date createdTime = new Date();
        List<RedirectionReport> pageCheck = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        for(Page url:list){
            executor.submit(new Runnable() {

                @Override
                public void run() {

                    try {

                        int  code =CheckHTTPResponse.verifyHttpMessage(url.getUrl());
                        if(code== HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM){
                            URL siteURL = new URL(url.getUrl());
                            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("User-Agent","Mozilla/5.0 ");
                            String message = connection.getResponseMessage();
                            String newUrl = connection.getHeaderField("Location");
                            connection = (HttpURLConnection) new URL(newUrl).openConnection();
                            connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                            connection .addRequestProperty("User-Agent", "Mozilla");
                            connection.addRequestProperty("Referer", "google.com");
                            String codeNew = ""+code;
                            RedirectionReport link = new RedirectionReport(code, url.getUrl(), message ,newUrl);
                            link.setPageOption(option);
                            link.setCreatedTime(createdTime);
                            pageCheck.add(link);
                        }
                    }catch (IOException e){
                        Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }
        executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        return pageCheck;
    }

    @Override
    public Map<String, Object> getHistoryRedirectionTestReport(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Date time = new Date(request.getReportTime());
        List<RedirectionReport> redirectionReports = linkRedirectionRepository.findALlByCreatedTime(time);
        res.put("action",Constant.SUCCESS);
        res.put("data",redirectionReports);
        return res;
    }

    @Override
    public Map<String, Object> getHistoryRedirectionTestList(RequestCommonPOJO request) {
        List<RedirectionReport> list = linkRedirectionRepository.findAllGroupByCreatedTimeAndPageOption(request.getUserId(), request.getPageOptionId());
        Map<String, Object> res = new HashMap<>();
        List<Long> dates = new ArrayList<>();
        for(int i = 0 ; i< list.size();i++)
        {
            dates.add(list.get(i).getCreatedTime().getTime());
        }
        res.put("action",Constant.SUCCESS);
        res.put("data",dates);
        return res;
    }


    //check redirection for Server Behavior TruongLN

    public  List<RedirectionReport> redirectionTest(UrlPOJO[] list) throws IOException {
        List<RedirectionReport> pageCheck = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.length);
        List<Thread> listThread = new ArrayList<>();
        for(UrlPOJO url:list){
            listThread.add(new Thread(){
                public void run() {
                    try {
                        int  code =CheckHTTPResponse.verifyHttpMessage(url.getUrl());
                        if(code== HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM){
                            URL siteURL = new URL(url.getUrl());
                            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("User-Agent","Mozilla/5.0 ");
                            String message = connection.getResponseMessage();
                            String newUrl = connection.getHeaderField("Location");
                            connection = (HttpURLConnection) new URL(newUrl).openConnection();
                            connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                            connection .addRequestProperty("User-Agent", "Mozilla");
                            connection.addRequestProperty("Referer", "google.com");
                            String codeNew = ""+code;
                            RedirectionReport link = new RedirectionReport(code, url.getUrl(), message ,newUrl);

                            pageCheck.add(link);
                        }
                    }catch (IOException e){
                        Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, e);
                    }

                }});

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
                Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return pageCheck;
    }

    public String getTitle(String url){
        String titleUrl="";
        try {
            Document doc = Jsoup.connect(url).get();
            titleUrl=doc.title();
        }catch (Exception ex){
        }
        return titleUrl;
    }


    public String getCanonicalUrl(String url){
        String canUrl ="";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements canoElements = doc.getElementsByTag("link");
            for(Element e: canoElements){
                String rel = e.attr("rel");
                if(rel.equals("canonical")){
                    canUrl = e.attr("href");
                }
            }
        }catch (Exception ex){
        }
        return canUrl;
    }

//    public int getStatus(String url) throws IOException {
//        int  result = 0;
//        int code = 200;
//        try {
//            URL siteURL = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("User-Agent","Mozilla/5.0 ");
//            code = connection.getResponseCode();
//            String message = connection.getResponseMessage();
//            System.out.println("Message respone"+message);
//            System.out.println("Code respone"+code );
//
//            if(code ==200){
//                result =  code;
//            }
//            else if  (code == HttpURLConnection.HTTP_MOVED_PERM|| code== HttpURLConnection.HTTP_MOVED_TEMP){
//                result = code;
//
//
//                String newUrl = connection.getHeaderField("Location");
//                connection = (HttpURLConnection) new URL(newUrl).openConnection();
//                connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
//                connection .addRequestProperty("User-Agent", "Mozilla");
//                connection.addRequestProperty("Referer", "google.com");
//
//                System.out.println("Addresses: "+url+" -Redirect to URL : " + newUrl+": Type: "+message);
//            }
//            else{
//                result =  code;
//            }
//        } catch (Exception e) {
//            result=404;
//        }
//        System.out.println(result);
//        return result;
//
//    }

    public List<ContactReport> getContactDetail(List<Page> list, PageOption option) throws InterruptedException {
        Date createdTime = new Date();
        List<ContactReport> list1 = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());

        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);
        for (Page newList : list){
            executor.submit(new Runnable() {

                @Override
                public void run() {

                    try {

                        String url = newList.getUrl();
                        int codeRespone = CheckHTTPResponse.verifyHttpMessage(url);
                        if(codeRespone<400 || codeRespone>=500){
                            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
                            doc.body().text();
                            System.out.println("--------");
                            String patternPhone0 = "\\((\\d{3})\\)[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})";
                            String patternPhone1 = "\\d{4}[-.\\s]?\\d{3}[-.\\s]?\\d{3}";
                            String patternPhone2 = "\\(?\\d{3}\\)?([ ]\\d{1})?[ ]\\d{3,4}[ ]\\d{4}";
                            String patternPhone3 = "\\((\\d{4})\\)[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})";
                            String patternPhone4 = "[+]\\d{2}?[-.\\s]?\\d{2,3}?[-.\\s]?\\d{4}[-.\\s]?\\d{2,4}";
                            String patternPhone5 = "\\d{2}?[ ]?\\d{3}[ ]\\d{4}[ ]\\d{3,4}";
                            String patternPhone6 = "\\d{4}[.]\\d{4}[.]\\d{3}";
                            String patternPhone7 = "\\d{3}[.][ ]\\d{8}";
                            String patternPhone8 = "[+]\\d{2}[ ]\\(\\d{2}\\)[ ]\\d{1}[ ]\\d{3}[ ]\\d{4}";
                            String patternPhone9 = "\\d{3}-\\d{3}-\\d{4}";
                            String patternphone10 = "\\d{3}[ ]\\d{2}[ ]\\d{3}[ ]\\d{3}";
                            String patternPhone11 = "\\(\\d{3}\\)[ ]\\d{2}[ ]\\d{2}[ ]\\d{2}[ ]\\d{2}";
                            String patternPhone12 = "[+]\\d{2,3}[ ]\\(\\d{1}\\)\\d{2}[ ]\\d{4}[ ]\\d{4}";
                            Pattern pattern = Pattern.compile(patternPhone0);
                            Matcher matcher = pattern.matcher(doc.wholeText());
                            while (matcher.find()) {
//                                System.out.println("Phone0: " + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);

                            }

                            pattern = Pattern.compile(patternPhone1);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("Phone1: " + matcher.group() + " . " + matcher.start() + " " + matcher.end()+" ");
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setCreatedTime(createdTime);
                                phoneNumber.setPageOption(option);
                                list1.add(phoneNumber);
                            }

                            pattern = Pattern.compile(patternPhone2);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 2:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }

                            pattern = Pattern.compile(patternPhone3);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 3:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }

                            pattern = Pattern.compile(patternPhone4);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 4:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }

                            pattern = Pattern.compile(patternPhone5);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 5:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }

                            pattern = Pattern.compile(patternPhone6);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 6:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }

                            pattern = Pattern.compile(patternPhone7);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 7:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }

                            pattern = Pattern.compile(patternPhone8);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 8:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }
                            pattern = Pattern.compile(patternPhone9);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 9:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }
                            pattern = Pattern.compile(patternphone10);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 10:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }
                            pattern = Pattern.compile(patternPhone11);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 11:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }
                            pattern = Pattern.compile(patternPhone12);
                            matcher = pattern.matcher(doc.wholeText());
                            // check all occurance
                            while (matcher.find()) {
//                                System.out.println("PHONE 11:" + matcher.group());
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Phone");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }
                            pattern = Pattern.compile("\\w+([.]|[-])?+\\w+@\\w+([-]\\w+)?[.]\\w+([.]\\w+)?+([.]\\w+)?");
                            matcher = pattern.matcher(doc.body().text());
                            while (matcher.find()) {
                                ContactReport phoneNumber = new ContactReport(matcher.group(),url,"Mail");
                                phoneNumber.setPageOption(option);
                                phoneNumber.setCreatedTime(createdTime);
                                list1.add(phoneNumber);
                            }
                        }

                    }catch (Exception ex){
                        Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }});
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        return  list1;
    }
}
