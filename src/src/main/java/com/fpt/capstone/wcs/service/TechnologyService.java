package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.JSInfo;
import com.fpt.capstone.wcs.model.LinkRedirection;
import com.fpt.capstone.wcs.model.ServerBehavior;
import com.fpt.capstone.wcs.model.Url;
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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

public class TechnologyService {

    public List<JSInfo> jsTestService(Url[] url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ngoct\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list JS info
        List<JSInfo> resultList = new ArrayList<>();
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
                                messages=entry.getMessage().toString().replace(matcher.group(0), "");
                            }

                            resultList.add(new JSInfo(messages, entry.getLevel().toString(), u.getUrl()));
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

    public ServerBehavior checkServerBehavior(Url url) throws IOException {
        ServerBehavior result = new ServerBehavior();
        System.out.println(url.getUrl());
        boolean isRedirectWWW = checkIsRedirectToWWW(url.getUrl());
        boolean isAllPageSSL = true;
//        boolean is
        result.setRedirectWWW(isRedirectWWW);
        result.setAllPageSSL(true);
        result.setExistErrorPage(true);
        result.setRedirectHTTPS(true);
        return result;
    }

    private boolean checkIsRedirectToWWW(String url) throws IOException {
        boolean result = false;
        if(url.toLowerCase().contains("www."))
        {
            url = url.replace("www.","");
        }
        System.out.println(url);
        Url[] urls = new Url[1];
        urls[0] = new Url(url);
        ContentService content = new ContentService();
        List<LinkRedirection> redirections = content.redirectionTest(urls);
        LinkRedirection instance = redirections.get(0);
        String urlRedirect =instance.getDriectToUrl();
        System.out.println(urlRedirect);
        if(urlRedirect.toLowerCase().contains("www"))
        {
            url = url.replace("https://","");
            url = url.replace("http://","");
            urlRedirect = urlRedirect.replace("https://","");
            urlRedirect = urlRedirect.replace("http://","");
            urlRedirect = urlRedirect.replace("www.","");
            if(urlRedirect.equals(url))
            {
                result=true;
            }
        }
        return result;
    }
}
