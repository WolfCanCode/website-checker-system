package com.fpt.capstone.wcs.service.report.technology;

import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
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
import com.fpt.capstone.wcs.repository.report.technology.ServerBehaviorRepository;
import com.fpt.capstone.wcs.repository.website.CookieDataRepository;
import com.fpt.capstone.wcs.repository.report.technology.CookieRepository;
import com.fpt.capstone.wcs.repository.report.technology.FaviconRepository;
import com.fpt.capstone.wcs.repository.report.technology.JSRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.service.report.content.ContentService;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateService;
import com.fpt.capstone.wcs.model.pojo.RequestReportPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;

import com.fpt.capstone.wcs.utils.CheckHTTPResponse;
import com.fpt.capstone.wcs.utils.Constant;
import org.apache.catalina.Server;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
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

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class TechnologyImpl implements TechnologyService {
    final PageOptionRepository pageOptionRepository;

    final AuthenticateService authenticate;

    final FaviconRepository faviconRepository;

    final JSRepository jsRepository;

    final CookieRepository cookieRepository;

    final CookieDataRepository cookieDataRepository;

    final ServerBehaviorRepository serverBehaviorRepository;

    final ContentService contentService; //reuse redirection

    @Autowired
    public TechnologyImpl(PageOptionRepository pageOptionRepository, AuthenticateService authenticate, FaviconRepository faviconRepository, JSRepository jsRepository, CookieRepository cookieRepository, CookieDataRepository cookieDataRepository, ContentService contentService, ServerBehaviorRepository serverBehaviorRepository) {
        this.pageOptionRepository = pageOptionRepository;
        this.authenticate = authenticate;
        this.faviconRepository = faviconRepository;
        this.jsRepository = jsRepository;
        this.cookieRepository = cookieRepository;
        this.cookieDataRepository = cookieDataRepository;
        this.contentService = contentService;
        this.serverBehaviorRepository = serverBehaviorRepository;
    }

    @Override
    public Map<String, Object> getfaviconTest(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }
            if (request.getPageOptionId() != -1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                String urlRoot = "";
                for (int i = 0; i < pages.size(); i++) {
                    Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(pages.get(i).getUrl());
                    while (matcher.find()) {
                        urlRoot = matcher.group();
                    }
                }
                List<FaviconReport> resultList = checkFavicon(pages, pageOption, urlRoot);
                faviconRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("favicontestReport", resultList);
                return res;
            } else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                String urlRoot = "";
                for (int i = 0; i < pages.size(); i++) {
                    Pattern pattern = Pattern.compile("(http\\:|https\\:)//([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(pages.get(i).getUrl());
                    while (matcher.find()) {
                        urlRoot = matcher.group();
                    }
                }

                List<FaviconReport> resultList = checkFavicon(pages, null, urlRoot);
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
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }

            if (request.getPageOptionId() != -1) {
                FaviconReport contactReport = faviconRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (contactReport != null) {
                    Date lastedCreatedTime = contactReport.getCreatedTime();
                    List<FaviconReport> resultList = faviconRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("favicontestReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                } else {
                    res.put("favicontestReport", new ArrayList<>());
                    res.put("action", Constant.SUCCESS);
                    return res;
                }
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
    public Map<String, Object> saveFaviconTest(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<FaviconReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<FaviconReport> optionalReport = faviconRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    FaviconReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<FaviconReport> results = faviconRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("favicontestReport", results);
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
    public Map<String, Object> getHistoryFaviconTestReport(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Date time = new Date(request.getReportTime());
        List<FaviconReport> faviconReports = faviconRepository.findALlByCreatedTime(time);
        res.put("action",Constant.SUCCESS);
        res.put("data",faviconReports);
        return res;
    }

    @Override
    public Map<String, Object> getHistoryFaviconTestList(RequestCommonPOJO request) {
        List<FaviconReport> list = faviconRepository.findAllGroupByCreatedTimeAndPageOption(request.getUserId(), request.getPageOptionId());
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
    public Map<String, Object> getJavaErrrorTest(RequestCommonPOJO request) throws InterruptedException {

        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }


            if (request.getPageOptionId() != -1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<JavascriptReport> resultList = jsTestService(pages, pageOption);
                jsRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("jsErrorReport", resultList);
                return res;
            } else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);

                List<JavascriptReport> resultList = jsTestService(pages, null);
                jsRepository.saveAll(resultList);
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
    public Map<String, Object> getLastestJS(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);

            if (pageOption != null) {
                JavascriptReport javascriptReport = jsRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (javascriptReport != null) {
                    Date lastedTime = javascriptReport.getCreatedTime();
                    List<JavascriptReport> resultList = jsRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedTime);
                    res.put("jsErrorReport", resultList);
                } else {
                    res.put("jsErrorReport", null);
                }
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("jsErrorReport", null);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> saveJSTestReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<JavascriptReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<JavascriptReport> optionalReport = jsRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    JavascriptReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<JavascriptReport> results = jsRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("jsErrorReport", results);
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
    public Map<String, Object> getHistoryJSTestReport(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Date time = new Date(request.getReportTime());
        List<JavascriptReport> javascriptReports = jsRepository.findALlByCreatedTime(time);
        res.put("action",Constant.SUCCESS);
        res.put("data",javascriptReports);
        return res;
    }

    @Override
    public Map<String, Object> getHistoryJSTestList(RequestCommonPOJO request) {
        List<JavascriptReport> list = jsRepository.findAllGroupByCreatedTimeAndPageOption(request.getUserId(), request.getPageOptionId());
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
    public Map<String, Object> getCookies(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(request);
        if (userWebsite != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), userWebsite.getWebsite(), false);
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }

            if (request.getPageOptionId() != -1) { //page option list is null
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
            } else {
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
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }
            if (request.getPageOptionId() != -1) {
                CookieReport cookieReport = cookieRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (cookieReport != null) {
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

    @Override
    public Map<String, Object> getHistoryCookiesTestReport(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Date time = new Date(request.getReportTime());
        List<CookieReport> cookieReports = cookieRepository.findALlByCreatedTime(time);
        res.put("action",Constant.SUCCESS);
        res.put("data",cookieReports);
        return res;
    }

    @Override
    public Map<String, Object> getHistoryCookiesTestList(RequestCommonPOJO request) {
        List<CookieReport> list = cookieRepository.findAllGroupByCreatedTimeAndPageOption(request.getUserId(), request.getPageOptionId());
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

                        String url = p.getUrl();
                        int codeRespone = CheckHTTPResponse.verifyHttpMessage(url);
                        if (codeRespone < 400) {
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
                        }


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
                    CookieReport cookieReport = new CookieReport(cookieList.get(i).getCookieName(), cookieList.get(i).getCategory(), cookieList.get(i).getParty(), cookieList.get(i).getDescription());
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
        Date currTime = new Date();
        for (Page u : list) {
            executor.submit(new Runnable() {
                @Override
                public void run() {

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
                    Set<String> errorStrings = new HashSet<>();
                    errorStrings.add("SyntaxError");
                    errorStrings.add("EvalError");
                    errorStrings.add("ReferenceError");
                    errorStrings.add("RangeError");
                    errorStrings.add("TypeError");
                    errorStrings.add("URIError");
                    LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
                    for (LogEntry entry : logEntries) {
                        for (String errorString : errorStrings) {
                            if (entry.getMessage().contains(errorString)) {
                                String pattern = "(http(.*?)\\s)";
                                Pattern pt = Pattern.compile(pattern);
                                Matcher matcher = pt.matcher(entry.getMessage().toString());
                                String messages = "";
                                if (matcher.find()) {
                                    messages = entry.getMessage().toString().replace(matcher.group(0), "");
                                }

                                messages = messages.replace(messages.split(" ")[0], "");
                                JavascriptReport report = new JavascriptReport(messages + "WCSLINK" + entry.getMessage().split(" ")[0] + " (" + entry.getMessage().split(" ")[1] + ") ", errorString.split("Error")[0], u.getUrl());
                                report.setPageOption(option);
                                report.setCreatedTime(currTime);
                                resultList.add(report);
                            }
                        }
                    }
                    driver.quit();
                }

            });


        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        return resultList;

    }


    public List<FaviconReport> checkFavicon(List<Page> list, PageOption option, String urlRoot) {
        Date createdTime = new Date();
        List<FaviconReport> resultList = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(Constant.MAX_THREAD);

        for (int i = 0; i < list.size(); i++) {
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
//                            System.out.println("Favicon URL: " + urlFaviconMethod1 + " Message: " + httpMessage + " Capacity: " + capacity.length);
                            flagMethod1 = true;
                        }
                    }
                    if (flagMethod1 == true) {
                        int codeResspone = CheckHTTPResponse.verifyHttpMessage(urlNew);
                        if (codeResspone < 400) {

//                            System.out.println(urlNew.startsWith(urlRoot));
                            if (urlNew.startsWith(urlRoot)) {
                                FaviconReport faviconMethod1 = new FaviconReport(urlFaviconMethod1, urlNew, "any", "16x16");
                                faviconMethod1.setPageOption(option);
                                faviconMethod1.setCreatedTime(createdTime);
                                resultList.add(faviconMethod1);
                            } else {
                                FaviconReport faviconMethod1 = new FaviconReport("External Link", urlNew, "", "");
                                faviconMethod1.setPageOption(option);
                                faviconMethod1.setCreatedTime(createdTime);
                                resultList.add(faviconMethod1);
                            }
                        }

                    } else if (flagMethod1 == false) {
                        try {
                            int codeResspone = CheckHTTPResponse.verifyHttpMessage(urlNew);
//                            System.out.println(urlNew);
                            if (codeResspone < 400) {

                                Document doc = Jsoup.connect(urlNew).ignoreContentType(true).get();
                                Elements elem = doc.head().select("link[rel~=(SHORTCUT ICON|shortcut icon|Shortcut Icon|ICON|Icon|icon|APPLE-TOUCH-ICON-PRECOMPOSED|apple-touch-icon-precomposed|Apple-Touch-Icon-Precomposed|nokia-touch-icon)]");
                                System.out.println(elem.size());
                                if (elem.size() == 0) {
                                    FaviconReport favicon = new FaviconReport("Missing Favicon", urlNew, "", "undefine");
                                    favicon.setPageOption(option);
                                    favicon.setCreatedTime(createdTime);
                                    resultList.add(favicon);
                                }
                                for (Element element : elem) {
                                    String size = element.attr("sizes");
                                    if (size.equals("")) {
                                        size = "undefine";
                                    }
                                    String rel = element.attr("rel");
                                    String href = elem.attr("href");
                                    int code = CheckHTTPResponse.verifyHttpMessage(href);
                                    if (code < 400) {
                                        FaviconReport faviconMethod2 = new FaviconReport(href, urlNew, rel, size);
                                        faviconMethod2.setPageOption(option);
                                        faviconMethod2.setCreatedTime(createdTime);
                                        resultList.add(faviconMethod2);
                                    }
                                    if (code >= 400) {
//                                        System.out.println("vao vao khac 200");
                                        String urlFavAgain = urlRoot + href;
                                        int checkFaviconResponeAgain = CheckHTTPResponse.verifyHttpMessage(urlFavAgain);
                                        if (checkFaviconResponeAgain < 400) {
                                            FaviconReport faviconAgain = new FaviconReport(urlFavAgain, urlNew, rel, size);
                                            faviconAgain.setPageOption(option);
                                            faviconAgain.setCreatedTime(createdTime);
                                            resultList.add(faviconAgain);
                                        }
                                        if (checkFaviconResponeAgain >= 400) {
                                            String urlFavLast = "https:" + href;
                                            int checkFaviconResponeLast = CheckHTTPResponse.verifyHttpMessage(urlFavLast);
                                            if (checkFaviconResponeLast < 400) {
                                                FaviconReport faviconLast = new FaviconReport(urlFavLast, urlNew, rel, size);
                                                faviconLast.setPageOption(option);
                                                faviconLast.setCreatedTime(createdTime);
                                                resultList.add(faviconLast);
                                            } else {
                                                FaviconReport faviconLast = new FaviconReport("Missing Favicon", urlNew, "", "");
                                                faviconLast.setPageOption(option);
                                                faviconLast.setCreatedTime(createdTime);
                                                resultList.add(faviconLast);
                                            }
                                        }
                                    }

                                }
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(TechnologyImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Logger.getLogger(TechnologyImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return resultList;
    }

    private byte[] getBytes(String url) {
        byte[] b = new byte[0];
        try {
            URL urlTesst = new URL(url);
            HttpURLConnection uc = (HttpURLConnection) urlTesst.openConnection();
            uc.setRequestMethod("GET");
            uc.setRequestProperty("User-Agent","Mozilla/5.0 ");
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


    @Override
    public Map<String, Object> getServerBehavior(RequestCommonPOJO request) throws IOException, InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }


            if (request.getPageOptionId() != -1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<ServerBehaviorReport> resultList = behaviorService(pages, pageOption);
                serverBehaviorRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("serverBehaviorReport", resultList);
                return res;
            } else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);

                List<ServerBehaviorReport> resultList = behaviorService(pages, null);
                serverBehaviorRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("serverBehaviorReport", resultList);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    public List<ServerBehaviorReport> behaviorService(List<Page> list, PageOption option) throws InterruptedException, IOException {
        Date currentTime = new Date();
        List<ServerBehaviorReport> resultList = new ArrayList<>();
        for (Page p : list) {
            String url = p.getUrl();
            boolean isSSL = isValidCertificate(url);
            boolean isRedirectHttps = isRedirectHttps(url);
            boolean isRedirectWWw = isRedirectWWW(url);
            ServerBehaviorReport report = new ServerBehaviorReport();
            report.setUrl(url);
            report.setPageSSL(isSSL);
            report.setRedirectHTTPS(isRedirectHttps);
            report.setRedirectWWW(isRedirectWWw);
            report.setPageOption(option);
            report.setCreatedTime(currentTime);
            resultList.add(report);
        }

        return resultList;
    }

    public boolean isValidCertificate(String ur) throws IOException {
        URL url = new URL(ur.replace("http://", "https://"));
        HttpsURLConnection con;
        try {
            con = (HttpsURLConnection) url.openConnection();
            con.connect();
            con.disconnect();
            return true;
        } catch (SSLException e) {
            return false;
        }
    }

    public boolean isRedirectHttps(String url) throws IOException {
        String urlWithoutHttp = "";
        url = url.toLowerCase();
        if (url.contains("https://")) {
            urlWithoutHttp = url.replace("https://", "http://");
        } else {
            urlWithoutHttp = url;
        }
        String urlDirectsTo = CheckHTTPResponse.getURLDirectsTo(urlWithoutHttp);
        return urlDirectsTo.contains("https");

    }

    public boolean isRedirectWWW(String url) throws IOException {
        String urlWithoutWWW = "";
        url = url.toLowerCase();
        if (url.contains("www")) {
            urlWithoutWWW = url.replace("https://www.", "http://");
        } else {
            urlWithoutWWW = url;
        }
        System.out.println(urlWithoutWWW);
        String urlDirectsTo = CheckHTTPResponse.getURLDirectsTo(urlWithoutWWW);
        System.out.println(urlDirectsTo);

        return urlDirectsTo.contains("www");
    }


    @Override
    public Map<String, Object> saveServerBehaviorReport(RequestReportPOJO request) {
        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {
            List<ServerBehaviorReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<ServerBehaviorReport> optionalReport = serverBehaviorRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    ServerBehaviorReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<ServerBehaviorReport> results = serverBehaviorRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("serverBehaviorReport", results);
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
    public Map<String, Object> getLastestServerBehavior(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }

            if (request.getPageOptionId() != null) {
                ServerBehaviorReport serverBehaviorReport = serverBehaviorRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (serverBehaviorReport != null) {
                    Date lastedDate = serverBehaviorReport.getCreatedTime();
                    List<ServerBehaviorReport> resultList = serverBehaviorRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedDate);
                    res.put("serverBehaviorReport", resultList);
                } else {
                    res.put("serverBehaviorReport", null);
                }
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                res.put("serverBehaviorReport", new ArrayList<>());
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getHistoryServerBehaviorTestReport(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Date time = new Date(request.getReportTime());
        List<ServerBehaviorReport> serverBehaviorReports = serverBehaviorRepository.findALlByCreatedTime(time);
        res.put("action",Constant.SUCCESS);
        res.put("data",serverBehaviorReports);
        return res;
    }

    @Override
    public Map<String, Object> getHistoryServerBehaviorTestList(RequestCommonPOJO request) {
        List<ServerBehaviorReport> list = serverBehaviorRepository.findAllGroupByCreatedTimeAndPageOption(request.getUserId(), request.getPageOptionId());
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


}
