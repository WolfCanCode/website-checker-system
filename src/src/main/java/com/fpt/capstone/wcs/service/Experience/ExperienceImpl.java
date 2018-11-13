package com.fpt.capstone.wcs.service.Experience;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.PageOptionRepository;
import com.fpt.capstone.wcs.repository.SpeedtestRepository;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExperienceImpl implements ExperienceService {

    final
    Authenticate authenticate;
    final
    PageOptionRepository pageOptionRepository;
    final
    SpeedtestRepository speedtestRepository;

    @Autowired
    public ExperienceImpl(Authenticate authenticate, PageOptionRepository pageOptionRepository, SpeedtestRepository speedtestRepository) {
        this.authenticate = authenticate;
        this.pageOptionRepository = pageOptionRepository;
        this.speedtestRepository = speedtestRepository;
    }

    @Override
    public Map<String, Object> doSpeedTest(RequestCommonPOJO request) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if(pageOption==null){
                request.setPageOptionId((long)-1);
            }
            if(request.getPageOptionId()!=-1) { //page option list is null
                List<Page> pages = pageOption.getPages();
                List<SpeedTestReport> resultList = speedTestService(pages, pageOption);
                speedtestRepository.removeAllByPageOption(pageOption);
                speedtestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("speedtestReport", resultList);
                return res;
            }
            else {
                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(website.getUrl());
                page.setType(1);
                pages.add(page);
                List<SpeedTestReport> resultList = speedTestService(pages, null);
                speedtestRepository.saveAll(resultList);
                res.put("action", Constant.SUCCESS);
                res.put("speedtestReport", resultList);
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
                List<SpeedTestReport> resultList = speedtestRepository.findAllByPageOption(pageOption);
                res.put("speedtestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            } else {
                List<SpeedTestReport> resultList = speedtestRepository.findAllByPageOptionAndUrl(null, website.getUrl());
                res.put("speedtestReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    public List<SpeedTestReport> speedTestService(List<Page> list, PageOption option) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ngoct\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list speed info
        List<SpeedTestReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.size());
        List<Thread> listThread = new ArrayList<>();
        for (Page p : list) {
            if (p.getType() == 1) {
                listThread.add(new Thread() {
                    public void run() {
                        try {
                            gate.await();
                            System.out.println("start testing url= " + p.getUrl());
                            //DesiredCapabilities
                            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                            LoggingPreferences logPrefs = new LoggingPreferences();
                            logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
                            ChromeOptions chromeOptions = new ChromeOptions();
                            chromeOptions.addArguments("--headless");
                            chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
                            WebDriver driver = new ChromeDriver(chromeOptions);//chay an

                            driver.get(p.getUrl());

                            List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
                            List<Double> entrySize = new ArrayList<>();
                            for (LogEntry entry : entries) {
                                Matcher dataLengthMatcher = Pattern.compile("encodedDataLength\":(.*?),").matcher(entry.getMessage());
                                if (dataLengthMatcher.find()) {
                                    entrySize.add(Double.parseDouble(dataLengthMatcher.group().split(":")[1].split(",")[0]));
                                }
                            }

                            double totalByte = new MathUtil().calculateSumDoubleList(entrySize);
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
                            SpeedTestReport speedTestReport = new SpeedTestReport(p.getUrl(), interactTime1 + "", loadTime1 + "", sizeTransferred1 + "");
                            speedTestReport.setPageOption(option);
                            resultList.add(speedTestReport);
                            driver.quit();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                });
            }
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
