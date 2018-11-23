package com.fpt.capstone.wcs.service.Technology;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.*;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    final Authenticate authenticate;

    final FaviconRepository faviconRepository;

    final JSCheckRepository jsCheckRepository;

    final CookieRepository cookieRepository;

    final CookieDataRepository cookieDataRepository;
    @Autowired
    public TechnologyImpl(PageOptionRepository pageOptionRepository, Authenticate authenticate, FaviconRepository faviconRepository, JSCheckRepository jsCheckRepository, CookieRepository cookieRepository, CookieDataRepository cookieDataRepository) {
        this.pageOptionRepository = pageOptionRepository;
        this.authenticate = authenticate;
        this.faviconRepository = faviconRepository;
        this.jsCheckRepository = jsCheckRepository;
        this.cookieRepository = cookieRepository;
        this.cookieDataRepository = cookieDataRepository;
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
    public Map<String, Object> getJavaErrrorTest(RequestCommonPOJO request) {

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
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }



            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();

                List<CookieReport> resultList = cookieService(pages, pageOption);
                cookieRepository.removeAllByPageOption(pageOption);
                cookieRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("cookieReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
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

                List<CookieReport> resultList = cookieRepository.findAllByPageOption(pageOption);
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
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }


    public List<CookieReport> cookieService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", Constant.CHROME_DRIVER);
        //Asign list JS info
        List<CookieData> cookieList = new ArrayList<>();

        cookieList = cookieDataRepository.findAll();

        List<CookieReport> resultList = new ArrayList<>();
        List<CookieReport> resultList11 = new ArrayList<>();

        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        List<String> cookieNames = new ArrayList<String>();

        for (Page p : list) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();

                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");

                        WebDriver driver = new ChromeDriver(chromeOptions);//chay an

                        driver.get(p.getUrl());

                        Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

                        //To find the number of cookies used by this site
                        System.out.println("Number of cookies in this site " + cookies.size());

                        for (org.openqa.selenium.Cookie cookie : cookies) {
                            // System.out.println(cookie.getName()+" "+cookie.getValue());
                            resultList11.add(new CookieReport(cookie.getName(), cookie.getDomain()));

                        }
                        System.out.println("size 1 " + resultList11.size());


                    } catch (InterruptedException | BrokenBarrierException e) {
                        Logger.getLogger(TechnologyImpl.class.getName()).log(Level.SEVERE, null, e);
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
                    resultList.add(cookieReport);
                    //resultList.add(new CookieReport(cookieList.get(i).getCookieName(), cookieList.get(i).getCategory(), cookieList.get(i).getParty(), cookieList.get(i).getDescription()));
                    a = 1;
                }
            }
            if (a == 0) {
                CookieReport cookieReport = new CookieReport(cookieName.getCookieName(), "Unknown", cookieName.getParty(), "The purpose of these cookies in unknown.");
                cookieReport.setPageOption(option);
                resultList.add(cookieReport);
                //resultList.add(new CookieReport(cookieName.getCookieName(), "Unknown", cookieName.getParty(), "The purpose of these cookies in unknown."));

            }

        }
//
        return resultList;
    }


    public List<JavascriptReport> jsTestService(List<Page> list, PageOption option) {
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
        }


        return resultList;

    }



    public List<FaviconReport> checkFavicon(List<Page> list, PageOption option, String urlRoot) {

        List<FaviconReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        for(int i=0; i<list.size();i++){
            String urlNew = list.get(i).getUrl();
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        boolean flagMethod1 = false;
                        String urlFaviconMethod1 = urlRoot + "/favicon.ico";
                        int httpMessage = verifyHttpMessage(urlFaviconMethod1);
                        if (httpMessage == 200) {
                            byte[] capacity = getBytes(urlFaviconMethod1);
                            if (capacity.length != 0) {
                                System.out.println("Favicon URL: " + urlFaviconMethod1 + " Message: " + httpMessage + " Capacity: " + capacity.length);
                                flagMethod1 = true;
                            }
                        }
                        if (flagMethod1 == true) {
                            System.out.println(urlNew.startsWith(urlRoot));
                            if(urlNew.startsWith(urlRoot)){
                                FaviconReport faviconMethod1 = new FaviconReport(urlFaviconMethod1, urlNew, "16x16");
                                faviconMethod1.setPageOption(option);
                                resultList.add(faviconMethod1);
                            }
                            else{
                                FaviconReport faviconMethod1 = new FaviconReport("External Link", urlNew, "");
                                faviconMethod1.setPageOption(option);
                                resultList.add(faviconMethod1);
                            }
                        } else if(flagMethod1 == false) {
                            try {
                                Document doc = Jsoup.connect(urlNew).ignoreContentType(true).get();
                                Elements elem = doc.head().select("link[rel~=(shortcut icon|icon|apple-touch-icon-precomposed|nokia-touch-icon)]");
                                System.out.println(elem.size());
                                if (elem.size() == 0) {
                                    FaviconReport favicon = new FaviconReport("Missing Favicon", urlNew, "undefine");
                                    favicon.setPageOption(option);
                                    resultList.add(favicon);
                                }
                                for (Element element : elem) {
                                    String size = element.attr("sizes");
                                    if (size.equals("")) {
                                        size = "undefine";
                                    }
                                    String href = elem.attr("href");
                                    int code = verifyHttpMessage(href);
                                    if (code == 200) {
                                        FaviconReport faviconMethod2 = new FaviconReport(href, urlNew, size);
                                        faviconMethod2.setPageOption(option);
                                        resultList.add(faviconMethod2);
                                    }
                                    if (code != 200) {
                                        System.out.println("vao vao khac 200");
                                        String urlFavAgain = urlRoot + href;
                                        int checkFaviconResponeAgain = verifyHttpMessage(urlFavAgain);
                                        if (checkFaviconResponeAgain == 200) {
                                            FaviconReport faviconAgain = new FaviconReport(urlFavAgain, urlNew, size);
                                            faviconAgain.setPageOption(option);
                                            resultList.add(faviconAgain);
                                        }
                                        if (checkFaviconResponeAgain != 200) {
                                            String urlFavLast = "https:" + href;
                                            int checkFaviconResponeLast = verifyHttpMessage(urlFavLast);
                                            if (checkFaviconResponeLast == 200) {
                                                FaviconReport faviconLast  = new FaviconReport(urlFavLast, urlNew, size);
                                                faviconLast.setPageOption(option);
                                                resultList.add(faviconLast);
                                            }
                                        }
                                    }

                                }
                            } catch (IOException ex) {
                                Logger.getLogger( TechnologyImpl .class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                        Logger.getLogger(TechnologyImpl.class.getName()).log(Level.SEVERE, null, e);
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
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    private int verifyHttpMessage(String url) {
        int message;
        try {
            URL urlTesst = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlTesst.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ");
            message = connection.getResponseCode();
        } catch (Exception e) {
            message = 404;
        }
        return message;
    }
}
