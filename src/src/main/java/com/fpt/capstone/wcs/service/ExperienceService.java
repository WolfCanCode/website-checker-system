package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.BrokenLink;
import com.fpt.capstone.wcs.model.BrokenPage;
import com.fpt.capstone.wcs.model.SpeedTest;
import com.fpt.capstone.wcs.model.Url;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExperienceService {
    public List<SpeedTest> speedTestService(Url[] url) throws InterruptedException {
        //Asign list speed info
        List<SpeedTest> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        System.out.println("start testing url= " + u.getUrl());
                        //BrowserMobProxy
                        BrowserMobProxy server = new BrowserMobProxyServer();
                        server.start(0);
                        server.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
                        server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
                        server.newHar(u.getUrl());
                        //PHANTOMJS_CLI_ARGS
                        List<String> cliArgsCap = new ArrayList<String>();
                        cliArgsCap.add("--proxy=localhost:" + server.getPort());
                        cliArgsCap.add("--ignore-ssl-errors=yes");
                        //DesiredCapabilities
                        DesiredCapabilities capabilities = new DesiredCapabilities();
                        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                        capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
                        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "//Users//mptt2//Downloads//phantomjs//bin//phantomjs");        //WebDriver
                        WebDriver driver = new PhantomJSDriver(capabilities);
                        driver.get(u.getUrl());
                        //HAR
                        Har har = server.getHar();
                        HarLog log = har.getLog();
                        List<HarEntry> listEntry = log.getEntries();
                        double sizeTransferred = 0;
                        for (HarEntry entry : listEntry) {
                            double requestSize = entry.getRequest().getHeadersSize() + entry.getRequest().getBodySize();
                            double reponseSize = entry.getResponse().getHeadersSize() + entry.getResponse().getBodySize();
                            sizeTransferred += requestSize + reponseSize;
//                            System.out.println("Entry: " + entry.getRequest().getMethod() + " " + entry.getRequest().getUrl() + " - " + requestSize + " B - " + reponseSize + " B");
                        }
                        //page load page interact
                        WebDriverWait myWait = new WebDriverWait(driver, 60);
                        ExpectedCondition<Boolean> conditionCheck = new ExpectedCondition<Boolean>() {
                            public Boolean apply(WebDriver input) {
                                return ((((JavascriptExecutor) input).executeScript("return document.readyState").equals("complete")));
                            }
                        };
                        myWait.until(conditionCheck);

                        Long loadPage = (Long) ((JavascriptExecutor) driver).executeScript(
                                "return performance.timing.loadEventEnd  - performance.timing.navigationStart;");
                        double loadTime = loadPage;
                        double loadTime1 = Math.floor(loadTime/1000 * 10) / 10;
                        Long interact = (Long) ((JavascriptExecutor) driver).executeScript(
                                "return performance.timing.domContentLoadedEventEnd  - performance.timing.navigationStart;");
                        double interactTime = interact;
                        double interactTime1 = Math.floor(interactTime/1000 * 10) / 10;
//                System.out.println("Total Page Load Time : " + loadTime1 + " milliseconds");
//                        System.out.println("Total Page Interact Time: " + interact + " milliseconds");
//                        System.out.println("Total Page Size: " + (float) sizeTransferred / 1000 / 1000 + "MBs");
                        double sizeTransferred1 = Math.floor(sizeTransferred/1000000 * 10) / 10;
                        resultList.add(new SpeedTest(u.getUrl(), interactTime1 + "", loadTime1 + "", sizeTransferred1 + ""));
                        driver.quit();
                        server.stop();
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

    public List<BrokenLink> brokenLinkService(Url[] url) throws InterruptedException {
        //Asign list Broken Link
        List<BrokenLink> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        HttpURLConnection connection = (HttpURLConnection) new URL(u.url).openConnection();
                        connection.connect();
                        String response = connection.getResponseMessage();
                        int respCode = connection.getResponseCode();
                        connection.disconnect();
                        System.out.println("URL: " + u.getUrl() + " returned " + response + " code " + respCode);
                        if(respCode == HttpURLConnection.HTTP_NOT_FOUND){

                            resultList.add(new BrokenLink(respCode, "https://www.nottingham.ac.uk/about", u.url));
                        }
                      //  resultList.add(new SpeedTest(u.url, interactTime1 + "", loadTime1 + "", sizeTransferred1 + ""));

                    }  catch (MalformedURLException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
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

    public List<BrokenPage> brokenPageService(Url[] url) throws InterruptedException {
        //Asign list Broken Page
        List<BrokenPage> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        HttpURLConnection connection = (HttpURLConnection) new URL(u.getUrl()).openConnection();
                        connection.connect();
                        String response = connection.getResponseMessage();
                        int respCode = connection.getResponseCode();
                        connection.disconnect();
                        System.out.println("URL: " + u.getUrl() + " returned " + response + " code " + respCode);
                        if(respCode == HttpURLConnection.HTTP_NOT_FOUND){

                            resultList.add(new BrokenPage(u.getUrl(), "Missing Page", respCode));
                        }

                        if(respCode == HttpURLConnection.HTTP_NO_CONTENT){

                            resultList.add(new BrokenPage(u.getUrl(), "Empty Page", respCode));
                        }


                    }  catch (MalformedURLException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
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

}
