package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.SpeedTest;
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

import java.util.ArrayList;
import java.util.List;

public class ExperienceService {
    public SpeedTest speedTestService(String url) {
        //BrowserMobProxy
        BrowserMobProxy server = new BrowserMobProxyServer();
        server.start(0);
        server.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
                server.newHar(url);
        //PHANTOMJS_CLI_ARGS
        List<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--proxy=localhost:" + server.getPort());
        cliArgsCap.add("--ignore-ssl-errors=yes");
        //DesiredCapabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:/Users/ngoct/Downloads/speedTest/speedTest/phantomjs-2.1.1-windows/bin/phantomjs.exe");        //WebDriver
        WebDriver driver = new PhantomJSDriver(capabilities);
        driver.get(url);

        //HAR
        Har har = server.getHar();
        HarLog log = har.getLog();
        List<HarEntry> listEntry = log.getEntries();
        long sizeTransferred = 0;
        for (HarEntry entry : listEntry) {
            float requestSize = entry.getRequest().getHeadersSize() + entry.getRequest().getBodySize();
            float reponseSize = entry.getResponse().getHeadersSize() + entry.getResponse().getBodySize();
            sizeTransferred += requestSize + reponseSize;
            System.out.println("Entry: " + entry.getRequest().getMethod() + " " + entry.getRequest().getUrl() + " - " + requestSize + " B - " + reponseSize + " B");
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
        Long interact = (Long) ((JavascriptExecutor) driver).executeScript(
                "return performance.timing.domContentLoadedEventEnd  - performance.timing.navigationStart;");

        System.out.println("Total Page Load Time : " + loadPage + " milliseconds");
        System.out.println("Total Page Interact Time: " + interact + " milliseconds");
        System.out.println("Total Page Size: " + (float) sizeTransferred / 1000 / 1000 + "MBs");
        driver.close();
        server.stop();
        return new SpeedTest(url,interact+"",loadPage+"",sizeTransferred+"");
    }
}
