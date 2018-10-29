package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.Url;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.Comparator.*;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class TechnologyService {

    public List<JavascriptReport> jsTestService(Url[] url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ngoct\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list JS info
        List<JavascriptReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        for (Url u : url) {
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
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
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

    public ServerBehaviorReport checkServerBehavior(Url url) throws IOException {
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
        Url[] urls = new Url[1];
        urls[0] = new Url(url1);
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
        urls[0] = new Url(url2);
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



    public List<CookieReport> cookieService(Url[] url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\trinhndse62136\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list JS info
        List<Cookie> cookieList = new ArrayList<>();
        cookieList.add(new Cookie("_ga", "Performance", "Google", "The world's most popular analytics tool."));
        cookieList.add(new Cookie("_gat", "Performance", "Google", "The world's most popular analytics tool."));
        cookieList.add(new Cookie("_dc_gtm_UA-72916918-1", "Performance", "Google", "The world's most popular analytics tool."));
        cookieList.add(new Cookie("_gat_UA-72916918-1", "Performance", "Google", "The world's most popular analytics tool."));
        cookieList.add(new Cookie("__utmt", "Performance", "Google", "The world's most popular analytics tool."));
        cookieList.add(new Cookie("__atuvc", "Performance", ".addthis.com", "These third party cookies are used to collect information about how visitors use the site."));
        cookieList.add(new Cookie("__atuvs", "Performance", ".addthis.com", "They are also used to limit the number of times you see an advertisement."));
        cookieList.add(new Cookie("DSID", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));
        cookieList.add(new Cookie("IDE", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));
        cookieList.add(new Cookie("_drt_", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));
        cookieList.add(new Cookie("id", "Performance", ".doubleclick.net", "This cookie is used for re-targeting, optimisation, reporting and attribution of online adverts."));


        cookieList.add(new Cookie("__utma", "Advertising", "Google", "These third party cookies are used to collect information about how visitors use the site."));
        cookieList.add(new Cookie("__utmb", "Advertising", "Google", "These third party cookies are used to collect information about how visitors use the site."));
        cookieList.add(new Cookie("__utmz", "Advertising", "Google", "These third party cookies are used to collect information about how visitors use the site."));

        cookieList.add(new Cookie("__atuvc", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("__atuvs", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("ana_svc", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("di2", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("dt", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("km_ai", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("km_lv", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("loc", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("siteaud", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("uid", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("um", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("uvc", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("vc", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("visitor_id92742", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("_conv_r", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));
        cookieList.add(new Cookie("_conv_v", "Comfort", ".addthis.com", "We use cookies to grant a more comfortable usage of the website."));


        cookieList.add(new Cookie("ASP.NET_SeesionId", "Essential", "Miscrosoft", "General purpose platform session cookie, used."));
        cookieList.add(new Cookie("WSS_FullScreenMode", "Essential", "Miscrosoft", "Microsoft SharePoint cookie for internal use of the application to indicate whether a page is shown in full screen mode."));


        List<CookieReport> resultList = new ArrayList<>();
        List<CookieReport> resultList11 = new ArrayList<>();

        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        List<String> cookieNames = new ArrayList<String>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();

                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");

                        WebDriver driver = new ChromeDriver(chromeOptions);//chay an

                        driver.get(u.getUrl());

                        Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

                        //To find the number of cookies used by this site
                        System.out.println("Number of cookies in this site " + cookies.size());

                        for (org.openqa.selenium.Cookie cookie : cookies) {
                            // System.out.println(cookie.getName()+" "+cookie.getValue());
                            resultList11.add(new CookieReport(cookie.getName(), cookie.getDomain()));

                        }
                        System.out.println("size 1 " + resultList11.size());


                    } catch (InterruptedException | BrokenBarrierException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
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

        List<CookieReport> resultList1 = resultList11.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(CookieReport::getCookieName).thenComparing(CookieReport::getParty))),
                        ArrayList::new));
        System.out.println("size 2 " + resultList1.size());
        int a = 0;
        for (CookieReport cookieName : resultList1) {
            a = 0;
            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieName.getCookieName().equals(cookieList.get(i).getCookieName())) {
                    resultList.add(new CookieReport(cookieList.get(i).getCookieName(), cookieList.get(i).getCategory(), cookieList.get(i).getParty(), cookieList.get(i).getDescription()));
                    a = 1;
                }
            }
            if (a == 0) {
                resultList.add(new CookieReport(cookieName.getCookieName(), "Unknown", cookieName.getParty(), "The purpose of these cookies in unknown."));

            }

        }
//
        return resultList;
    }

}
