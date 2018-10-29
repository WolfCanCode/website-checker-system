package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.entity.SpeedTestReport;
import com.fpt.capstone.wcs.model.Url;
import com.fpt.capstone.wcs.utils.MathUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExperienceService {

    public List<SpeedTestReport> speedTestService(Url[] url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ngoct\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list speed info
        List<SpeedTestReport> resultList = new ArrayList<>();
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
                        LoggingPreferences logPrefs = new LoggingPreferences();
                        logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--headless");
                        chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
                        WebDriver driver = new ChromeDriver(chromeOptions);//chay an

                        driver.get(u.getUrl());

                        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
                        List<Double> entrySize = new ArrayList<>();
                        for (LogEntry entry : entries) {
                            Matcher dataLengthMatcher = Pattern.compile("encodedDataLength\":(.*?),").matcher(entry.getMessage());
                            if(dataLengthMatcher.find()) {
                                entrySize.add(Double.parseDouble(dataLengthMatcher.group().split(":")[1].split(",")[0]));
                            }
                        }

                        double totalByte =  new MathUtil().calculateSumDoubleList(entrySize);
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
                        double loadTime1 = Math.floor(loadTime / 1000 * 10) / 10;
                        Long interact = (Long) ((JavascriptExecutor) driver).executeScript(
                                "return performance.timing.domContentLoadedEventEnd  - performance.timing.navigationStart;");
                        double interactTime = interact;
                        double interactTime1 = Math.floor(interactTime / 1000 * 10) / 10;
                        double sizeTransferred1 = Math.floor(totalByte / 1000000 * 10) / 10;
                        resultList.add(new SpeedTestReport(u.getUrl(), interactTime1 + "", loadTime1 + "", sizeTransferred1 + ""));
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
}
