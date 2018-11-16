package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.repository.CookieDataRepository;
import com.fpt.capstone.wcs.service.Experience.ExperienceImpl;
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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.Comparator.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class TechnologyService {

    @Autowired
    CookieDataRepository cookieDataRepository;

    public List<JavascriptReport> jsTestService(UrlPOJO[] url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ngoct\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list JS info
        List<JavascriptReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        for (UrlPOJO u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
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

                            resultList.add(new JavascriptReport(messages, entry.getLevel().toString(), u.getUrl()));
                        }
                        driver.quit();
                    } catch (InterruptedException | BrokenBarrierException e) {
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

    public ServerBehaviorReport checkServerBehavior(UrlPOJO url) throws IOException {
        ServerBehaviorReport result = new ServerBehaviorReport();
        System.out.println(url.getUrl());
        boolean isRedirectWWW = checkIsRedirect(url.getUrl())[0];
        boolean isRedirectHTTPS = checkIsRedirect(url.getUrl())[1];
//        boolean is
        result.setRedirectWWW(isRedirectWWW);
        result.setAllPageSSL(true);
        result.setExistErrorPage(true);
        result.setRedirectHTTPS(isRedirectHTTPS);
        return result;
    }

    private boolean[] checkIsRedirect(String url) throws IOException {
        boolean[] result= new boolean[2];
        result[0] = false;
        result[1] = false;
        String url1=url;
        String url2=url;
        if (url.toLowerCase().contains("www.")) {
            url1 = url1.replace("www.", "");
        }
        System.out.println(url1);
        UrlPOJO[] urls = new UrlPOJO[1];
        urls[0] = new UrlPOJO(url1);
        ContentService content = new ContentService();
        List<RedirectionReport> redirections = content.redirectionTest(urls);
        RedirectionReport instance = redirections.get(0);
        String urlRedirect = instance.getDriectToUrl();
        System.out.println(urlRedirect);
        if (urlRedirect.toLowerCase().contains("www")) {
            url1 = url1.replace("https://", "");
            url1 = url1.replace("http://", "");
            urlRedirect = urlRedirect.replace("https://", "");
            urlRedirect = urlRedirect.replace("http://", "");
            urlRedirect = urlRedirect.replace("www.", "");
            if (urlRedirect.equals(url1)) {
                result[0] = true;
            }
        }

        if(!url2.contains("www")){
            if(url2.contains("https://"))
            url2 = url2.replace("https://","http://www.");
            else url2 = url2.replace("http://","https://www.");

        } else {
            url2 = url2.replace("https://www","http://www");
        }
        urls[0] = new UrlPOJO(url2);
        ContentService content1 = new ContentService();
        List<RedirectionReport> redirections1 = content1.redirectionTest(urls);
        RedirectionReport instance1 = redirections1.get(0);
        String urlRedirect1 = instance1.getDriectToUrl();
        System.out.println("==="+urlRedirect1);
        if(urlRedirect1.toLowerCase().contains("https")) {
            System.out.println("===="+url2);
            System.out.println("===="+urlRedirect1);
            url = url.replace("https://", "");
            url = url.replace("www.", "");
            urlRedirect1 = urlRedirect1.replace("www.", "");
            urlRedirect1 = urlRedirect1.replace("https://", "");
            System.out.println("===="+"===="+url);
            System.out.println("===="+"===="+urlRedirect1);
            if(urlRedirect1.equals(url)){
                result[1] = true;
            }

        }
        return result;
    }



    public List<CookieReport> cookieService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\trinhndse62136\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list JS info
        List<CookieData> cookieList = new ArrayList<>();
       // System.out.println(cookieDataRepository.getOne(Long.parseLong(2+"")).getCookieName());
//        cookieList.add(new CookieData("_ga", "Performance", "Google", "This cookie is used to distinguish unique users by assigning a randomly generated number as a client identifier.."));
//        cookieList.add(new CookieData("_gat", "Performance", "Google", "This cookie is used to throttle the request rate - limiting the collection of data on high traffic sites."));
//        cookieList.add(new CookieData("_dc_gtm_UA-72916918-1", "Performance", "Google", "This cookie is used to distinguish unique users and throttle the request rate."));
//        cookieList.add(new CookieData("_gat_UA-72916918-1", "Performance", "Google", "The world's most popular analytics tool."));
//        cookieList.add(new CookieData("__utmt", "Performance", "Google", "These third party cookies are used to collect information about how visitors use the site."));
//        cookieList.add(new CookieData("__atuvc", "Performance", ".addthis.com", "This cookie is used to deliver adverts more relevant to user based on information about user's visit to this site and other sites."));
//        cookieList.add(new CookieData("__atuvs", "Performance", ".addthis.com", "This cookie is used to deliver adverts more relevant to user based on information about user's visit to this site and other sites."));
//        cookieList.add(new CookieData("DSID", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));
//        cookieList.add(new CookieData("IDE", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));
//        cookieList.add(new CookieData("_drt_", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));
//        cookieList.add(new CookieData("id", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));
//
//
//        cookieList.add(new CookieData("__utma", "Advertising", "Google", "These third party cookies are used to collect information about how visitors use the site."));
//        cookieList.add(new CookieData("__utmb", "Advertising", "Google", "These third party cookies are used to collect information about how visitors use the site."));
//        cookieList.add(new CookieData("__utmz", "Advertising", "Google", "These third party cookies are used to collect information about how visitors use the site."));
//
//        cookieList.add(new CookieData("__atuvc", "Comfort", ".addthis.com", "This cookie is used to enable visitors to share content with a range of networking and sharing platforms."));
//        cookieList.add(new CookieData("__atuvs", "Comfort", ".addthis.com", "This cookie is used to enable visitors to share content with a range of networking and sharing platforms."));
//        cookieList.add(new CookieData("ana_svc", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("di2", "Comfort", ".addthis.com", "This cookie is used to share content on website across popular social media networks."));
//        cookieList.add(new CookieData("dt", "Comfort", ".addthis.com", "This cookie is usedto allow website visitors to share content on various social networks."));
//        cookieList.add(new CookieData("km_ai", "Comfort", ".addthis.com", "This cookie is used to provide site owners with information about how visitors use the site."));
//        cookieList.add(new CookieData("km_lv", "Comfort", ".addthis.com", "This cookie is used to provide site owners with information about how visitors use the site."));
//        cookieList.add(new CookieData("loc", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("siteaud", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("uid", "Comfort", ".addthis.com", "This cookie provides a uniquely assigned, machine-generated user ID and gathers data about activity on the website."));
//        cookieList.add(new CookieData("um", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("uvc", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("vc", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("visitor_id92742", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("_conv_r", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//        cookieList.add(new CookieData("_conv_v", "Comfort", ".addthis.com", "This cookie is used to grant a more comfortable usage of the website."));
//
//
//        cookieList.add(new CookieData("ASP.NET_SessionId", "Essential", "Miscrosoft", "Usually used to maintain an anonymised user session by the server."));
//        cookieList.add(new CookieData("WSS_FullScreenMode", "Essential", "Miscrosoft", "Microsoft SharePoint cookie for internal use of the application to indicate whether a page is shown in full screen mode."));
        // cookieDataRepository.saveAll(cookieList);
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

    public static List<FaviconReport> checkFavicon(List<Page> list, PageOption option, String urlRoot) {
        List<FaviconReport> fav = new ArrayList();
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
        for ( Page urlNew : list) {
            if (flagMethod1 == true) {
                System.out.println(urlNew.getUrl().startsWith(urlRoot));
                if(urlNew.getUrl().startsWith(urlRoot)){
                    FaviconReport faviconMethod1 = new FaviconReport(urlFaviconMethod1, urlNew.getUrl(), "16x16");
                    faviconMethod1.setPageOption(option);
                    fav.add(faviconMethod1);
                }
                else{
                    FaviconReport faviconMethod1 = new FaviconReport("External Link", urlNew.getUrl(), "");
                    faviconMethod1.setPageOption(option);
                    fav.add(faviconMethod1);
                }
            } else {
                try {
                    Document doc = Jsoup.connect(urlNew.getUrl()).ignoreContentType(true).get();

                    Elements elem = doc.head().select("link[rel~=(shortcut icon|icon|apple-touch-icon-precomposed|nokia-touch-icon)]");
                    System.out.println(elem.size());
                    if (elem.size() == 0) {
                        FaviconReport favicon = new FaviconReport("Missing Favicon", urlNew.getUrl(), "undefine");
                        favicon.setPageOption(option);
                        fav.add(favicon);
                    }
                    for (Element element : elem) {
                        String size = element.attr("sizes");

                        if (size.equals("")) {
                            size = "undefine";
                        }
                        String href = elem.attr("href");
                        int code = verifyHttpMessage(href);
                        if (code == 200) {
                            FaviconReport favicon = new FaviconReport(href, urlNew.getUrl(), size);
                            favicon.setPageOption(option);
                            fav.add(favicon);
                            System.out.println("Favicon: " + href + " - Web Address: " + urlNew + " - Size: " + size + " http code: " + code);
                        }
                        if (code != 200) {
                            String urlFavAgain = urlRoot + href;
                            int checkFaviconResponeAgain = verifyHttpMessage(urlFavAgain);
                            if (checkFaviconResponeAgain == 200) {
                                FaviconReport favicon = new FaviconReport(urlFavAgain, urlNew.getUrl(), size);
                                favicon.setPageOption(option);
                                fav.add(favicon);
                                System.out.println("Favicon: " + urlFavAgain + " - Web Address: " + urlNew + " - Size: " + size + " http code: " + checkFaviconResponeAgain);
                            }
                            if (checkFaviconResponeAgain != 200) {
                                String urlFavLast = "https:" + href;
                                int checkFaviconResponeLast = verifyHttpMessage(urlFavLast);
                                if (checkFaviconResponeLast == 200) {
                                    FaviconReport favicon = new FaviconReport(urlFavLast, urlNew.getUrl(), size);
                                    favicon.setPageOption(option);
                                    fav.add(favicon);
                                    System.out.println("Favicon: " + urlFavLast + " - Web Address: " + urlNew + " - Size: " + size + " http code: " + checkFaviconResponeLast);
                                }
                            }
                        }

                    }
                } catch (IOException ex) {
                    Logger.getLogger( TechnologyService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return fav;
    }

    private static byte[] getBytes(String url) {
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

    private static int verifyHttpMessage(String url) {
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
