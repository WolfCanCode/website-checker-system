package com.fpt.capstone.wcs.service.report.technology;

import com.fpt.capstone.wcs.model.entity.report.technology.ServerBehaviorReport;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.report.technology.CookieReport;
import com.fpt.capstone.wcs.model.entity.report.technology.FaviconReport;
import com.fpt.capstone.wcs.model.entity.report.technology.JavascriptReport;
import com.fpt.capstone.wcs.model.entity.website.CookieData;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.website.CookieDataRepository;
import com.fpt.capstone.wcs.repository.report.technology.CookieRepository;
import com.fpt.capstone.wcs.repository.report.technology.FaviconRepository;
import com.fpt.capstone.wcs.repository.report.technology.JSCheckRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.service.report.content.ContentService;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateService;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;

import com.fpt.capstone.wcs.utils.CheckHTTPResponse;
import com.fpt.capstone.wcs.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class TechnologyImpl implements TechnologyService {
    final PageOptionRepository pageOptionRepository;

    final AuthenticateService authenticate;

    final FaviconRepository faviconRepository;

    final JSCheckRepository jsCheckRepository;

    final CookieRepository cookieRepository;

    final CookieDataRepository cookieDataRepository;

    final ContentService contentService; //reuse redirection
    @Autowired
    public TechnologyImpl(PageOptionRepository pageOptionRepository, AuthenticateService authenticate, FaviconRepository faviconRepository, JSCheckRepository jsCheckRepository, CookieRepository cookieRepository, CookieDataRepository cookieDataRepository, ContentService contentService) {
        this.pageOptionRepository = pageOptionRepository;
        this.authenticate = authenticate;
        this.faviconRepository = faviconRepository;
        this.jsCheckRepository = jsCheckRepository;
        this.cookieRepository = cookieRepository;
        this.cookieDataRepository = cookieDataRepository;
        this.contentService = contentService;
    }

    @Override
    public Map<String, Object> getfaviconTest(RequestCommonPOJO request) {
        Map<String,Object> res = new HashMap<>();
        Website website =authenticate.isAuthGetSingleSite(request);
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
                List<FaviconReport> resultList = checkFavicon(pages, pageOption, urlRoot);
                faviconRepository.removeAllByPageOption(pageOption);
                faviconRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("favicontestReport", resultList);
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

                List<FaviconReport> resultList = checkFavicon(pages, null,urlRoot);
                faviconRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("favicontestReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestFaviconTest(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                List<FaviconReport> resultList = faviconRepository.findAllByPageOption(pageOption);
                res.put("favicontestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<FaviconReport> resultList = faviconRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("favicontestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getJavaErrrorTest(RequestCommonPOJO request) throws InterruptedException {

        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<JavascriptReport> resultList = jsTestService(pages, pageOption);
                jsCheckRepository.removeAllByPageOption(pageOption);
                jsCheckRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("jsErrorReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);

                List<JavascriptReport> resultList =jsTestService(pages, null);
                jsCheckRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("jsErrorReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestSpeedTest(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }

            if(request.getPageOptionId()!=-1) {

                List<JavascriptReport> resultList = jsCheckRepository.findAllByPageOption(pageOption);
                res.put("jsErrorReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<JavascriptReport> resultList = jsCheckRepository.findAllByPageOption(null);
                res.put("jsErrorReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getCookies(RequestCommonPOJO request) throws InterruptedException {
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
                List<CookieReport> resultList = cookieService(pages, pageOption);
                cookieRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("cookieReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(userWebsite.getWebsite().getUrl());
                page.setType(1);
                pages.add(page);

                List<CookieReport> resultList = cookieService(pages, null);
                cookieRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("cookieReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getLastestCookies(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) {
                CookieReport cookieReport = cookieRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if(cookieReport != null){
                    Date lastedCreatedTime = cookieReport.getCreatedTime();
                    List<CookieReport> resultList = cookieRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("cookieReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                } else {
                    List<CookieReport> resultList = cookieRepository.findAllByPageOption(null);
                    res.put("cookieReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }

            } else {
                List<CookieReport> resultList = cookieRepository.findAllByPageOption(null);
                res.put("cookieReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveCookieReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {

            List<CookieReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<CookieReport> optionalReport = cookieRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    CookieReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<CookieReport> results = cookieRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("cookieReport", results);
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


    public List<CookieReport> cookieService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", Constant.CHROME_DRIVER);
        Date createdTime = new Date();
        List<CookieData> cookieList = new ArrayList<>();

        cookieList = cookieDataRepository.findAll();

        List<CookieReport> resultList = new ArrayList<>();
        List<CookieReport> resultList11 = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);

        for (Page p : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {


                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");

                        WebDriver driver = new ChromeDriver(chromeOptions);//chay an

                        driver.get(p.getUrl());

                        Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

                        //To find the number of cookies used by this site
                        System.out.println("Number of cookies in this site " + cookies.size());

                        for (org.openqa.selenium.Cookie cookie : cookies) {
                            // user.out.println(cookie.getName()+" "+cookie.getValue());
                            resultList11.add(new CookieReport(cookie.getName(), cookie.getDomain()));

                        }
                        System.out.println("size 1 " + resultList11.size());


                    } catch (Exception e) {
                        Logger.getLogger(TechnologyImpl.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        //delete duplicate cookie
        List<CookieReport> resultList1 = resultList11.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(CookieReport::getCookieName).thenComparing(CookieReport::getParty))),
                        ArrayList::new));
        System.out.println("size 2 " + resultList1.size());
        int a = 0;
        for (CookieReport cookieName : resultList1) {
            a = 0;
            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieName.getCookieName().equalsIgnoreCase(cookieList.get(i).getCookieName())) {
                    CookieReport cookieReport =  new CookieReport(cookieList.get(i).getCookieName(), cookieList.get(i).getCategory(), cookieList.get(i).getParty(), cookieList.get(i).getDescription());
                    cookieReport.setPageOption(option);
                    cookieReport.setCreatedTime(createdTime);
                    resultList.add(cookieReport);
                    //resultList.add(new CookieReport(cookieList.get(i).getCookieName(), cookieList.get(i).getCategory(), cookieList.get(i).getParty(), cookieList.get(i).getDescription()));
                    a = 1;
                }
            }
            if (a == 0) {
                CookieReport cookieReport = new CookieReport(cookieName.getCookieName(), "Unknown", cookieName.getParty(), "The purpose of these cookies in unknown.");
                cookieReport.setPageOption(option);
                cookieReport.setCreatedTime(createdTime);
                resultList.add(cookieReport);
                //resultList.add(new CookieReport(cookieName.getCookieName(), "Unknown", cookieName.getParty(), "The purpose of these cookies in unknown."));

            }

        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        return resultList;
    }


    public List<JavascriptReport> jsTestService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", Constant.CHROME_DRIVER);
        //Asign list JS info
        List<JavascriptReport> resultList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);

        for (Page u : list) {
            executor.submit(new Runnable() {
                @Override
                public void run()
                {

                        System.out.println("start testing url= " + u.getUrl());
                        //DesiredCapabilities
                        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                        LoggingPreferences loggingprefs = new LoggingPreferences();
                        loggingprefs.enable(LogType.BROWSER, Level.ALL);
                        capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");
                        WebDriver driver = new ChromeDriver(chromeOptions);

                        driver.get(u.getUrl());

                        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
                        for (LogEntry entry : logEntries) {
                            String pattern = "(http(.*?)\\s)";
                            Pattern pt = Pattern.compile(pattern);
                            Matcher matcher = pt.matcher(entry.getMessage().toString());
                            String messages = "";
                            if (matcher.find()) {
                                messages = entry.getMessage().toString().replace(matcher.group(0), "");
                            }
                            JavascriptReport report = new JavascriptReport(messages, entry.getLevel().toString(), u.getUrl());
                            report.setPageOption(option);
                            resultList.add(report);
                        }
                        driver.quit();
                    }

            });

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        }


        return resultList;

    }



    public List<FaviconReport> checkFavicon(List<Page> list, PageOption option, String urlRoot) {
        Date createdTime = new Date();
        List<FaviconReport> resultList = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);

        for(int i=0; i<list.size();i++){
            String urlNew = list.get(i).getUrl();
            executor.submit(new Runnable() {
                @Override
                public void run() {


                        boolean flagMethod1 = false;
                        String urlFaviconMethod1 = urlRoot + "/favicon.ico";
                        int httpMessage = CheckHTTPResponse.verifyHttpMessage(urlFaviconMethod1);
                        if (httpMessage == 200) {
                            byte[] capacity = getBytes(urlFaviconMethod1);
                            if (capacity.length != 0) {
                                System.out.println("Favicon URL: " + urlFaviconMethod1 + " Message: " + httpMessage + " Capacity: " + capacity.length);
                                flagMethod1 = true;
                            }
                        }
                        if (flagMethod1 == true) {
                            int codeResspone = CheckHTTPResponse.verifyHttpMessage(urlNew);
                            if(codeResspone<400||codeResspone>=500 ){
                                System.out.println(urlNew.startsWith(urlRoot));

                                if(urlNew.startsWith(urlRoot)){
                                    FaviconReport faviconMethod1 = new FaviconReport(urlFaviconMethod1, urlNew,"any", "16x16");
                                    faviconMethod1.setPageOption(option);
                                    faviconMethod1.setCreatedTime(createdTime);
                                    resultList.add(faviconMethod1);
                                }
                                else{
                                    FaviconReport faviconMethod1 = new FaviconReport("External Link", urlNew,"" ,"");
                                    faviconMethod1.setPageOption(option);
                                    faviconMethod1.setCreatedTime(createdTime);
                                    resultList.add(faviconMethod1);
                                }
                            }

                        } else if(flagMethod1 == false) {
                            try {
                                int codeResspone = CheckHTTPResponse.verifyHttpMessage(urlNew);
                                System.out.println(urlNew);
                                if(codeResspone<400||codeResspone>=500 ){
                                    Document doc = Jsoup.connect(urlNew).ignoreContentType(true).get();
                                    Elements elem = doc.head().select("link[rel~=(shortcut icon|icon|apple-touch-icon-precomposed|nokia-touch-icon)]");
                                    System.out.println(elem.size());
                                    if (elem.size() == 0) {
                                        FaviconReport favicon = new FaviconReport("Missing Favicon", urlNew, "","undefine");
                                        favicon.setPageOption(option);
                                        favicon.setCreatedTime(createdTime);
                                        resultList.add(favicon);
                                    }
                                    for (Element element : elem) {
                                        String size = element.attr("sizes");
                                        if (size.equals("")) {
                                            size = "undefine";
                                        }
                                        String rel =element.attr("rel");
                                        String href = elem.attr("href");
                                        int code = CheckHTTPResponse.verifyHttpMessage(href);
                                        if ( code<400||code>=500 ) {
                                            FaviconReport faviconMethod2 = new FaviconReport(href, urlNew,rel, size);
                                            faviconMethod2.setPageOption(option);
                                            faviconMethod2.setCreatedTime(createdTime);
                                            resultList.add(faviconMethod2);
                                        }
                                        if (code >=400 && code <500) {
                                            System.out.println("vao vao khac 200");
                                            String urlFavAgain = urlRoot + href;
                                            int checkFaviconResponeAgain = CheckHTTPResponse.verifyHttpMessage(urlFavAgain);
                                            if (checkFaviconResponeAgain == 200) {
                                                FaviconReport faviconAgain = new FaviconReport(urlFavAgain, urlNew,rel, size);
                                                faviconAgain.setPageOption(option);
                                                faviconAgain.setCreatedTime(createdTime);
                                                resultList.add(faviconAgain);
                                            }
                                            if (checkFaviconResponeAgain != 200) {
                                                String urlFavLast = "https:" + href;
                                                int checkFaviconResponeLast = CheckHTTPResponse.verifyHttpMessage(urlFavLast);
                                                if (checkFaviconResponeLast == 200) {
                                                    FaviconReport faviconLast  = new FaviconReport(urlFavLast, urlNew, rel, size);
                                                    faviconLast.setPageOption(option);
                                                    faviconLast.setCreatedTime(createdTime);
                                                    resultList.add(faviconLast);
                                                }
                                            }
                                        }

                                    }
                                }

                            } catch (IOException ex) {
                                Logger.getLogger( TechnologyImpl .class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                }});

        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Logger.getLogger(TechnologyImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return resultList;
    }

    private byte[] getBytes(String url) {
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
        } catch (IOException ex) {

        }

        return b;
    }

//    private int verifyHttpMessage(String url) {
//        int message;
//        try {
//            URL urlTesst = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) urlTesst.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ");
//            message = connection.getResponseCode();
//        } catch (Exception e) {
//            message = 404;
//        }
//        return message;
//    }


    public ServerBehaviorReport checkServerBehavior(UrlPOJO url) throws IOException {
        ServerBehaviorReport result = new ServerBehaviorReport();
        System.out.println(url.getUrl());
        boolean isRedirectWWW = checkIsRedirect(url.getUrl())[0];
        boolean isRedirectHTTPS = checkIsRedirect(url.getUrl())[1];
//        boolean is
        result.setRedirectWWW(isRedirectWWW);
        result.setPageSSL(true);
        result.setRedirectHTTPS(isRedirectHTTPS);
        return result;
    }

    private boolean[] checkIsRedirect(String url) throws IOException {
        boolean[] result= new boolean[2];
//        result[0] = false;
//        result[1] = false;
//        String url1=url;
//        String url2=url;
//        if (url.toLowerCase().contains("www.")) {
//            url1 = url1.replace("www.", "");
//        }
//        System.out.println(url1);
//        UrlPOJO[] urls = new UrlPOJO[1];
//        urls[0] = new UrlPOJO(url1);
//        List<RedirectionReport> redirections = ContentService.redirectionTest(urls);
//        RedirectionReport instance = redirections.get(0);
//        String urlRedirect = instance.getDriectToUrl();
//        System.out.println(urlRedirect);
//        if (urlRedirect.toLowerCase().contains("www")) {
//            url1 = url1.replace("https://", "");
//            url1 = url1.replace("http://", "");
//            urlRedirect = urlRedirect.replace("https://", "");
//            urlRedirect = urlRedirect.replace("http://", "");
//            urlRedirect = urlRedirect.replace("www.", "");
//            if (urlRedirect.equals(url1)) {
//                result[0] = true;
//            }
//        }
//
//        if(!url2.contains("www")){
//            if(url2.contains("https://"))
//                url2 = url2.replace("https://","http://www.");
//            else url2 = url2.replace("http://","https://www.");
//
//        } else {
//            url2 = url2.replace("https://www","http://www");
//        }
//        urls[0] = new UrlPOJO(url2);
//        List<RedirectionReport> redirections1 = contentService.redirectionTest(urls);
//        RedirectionReport instance1 = redirections1.get(0);
//        String urlRedirect1 = instance1.getDriectToUrl();
//        System.out.println("==="+urlRedirect1);
//        if(urlRedirect1.toLowerCase().contains("https")) {
//            System.out.println("===="+url2);
//            System.out.println("===="+urlRedirect1);
//            url = url.replace("https://", "");
//            url = url.replace("www.", "");
//            urlRedirect1 = urlRedirect1.replace("www.", "");
//            urlRedirect1 = urlRedirect1.replace("https://", "");
//            System.out.println("===="+"===="+url);
//            System.out.println("===="+"===="+urlRedirect1);
//            if(urlRedirect1.equals(url)){
//                result[1] = true;
//            }
//
//        }

        return result;
    }
}
