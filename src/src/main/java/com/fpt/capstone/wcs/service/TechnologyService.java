package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.*;
import com.fpt.capstone.wcs.model.entity.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

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

    public List<MissingFileReport> getMissingFile(Url[] url, String urlNew) throws InterruptedException {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(jpg|gif|jp2|jpeg|pbm|pcx|pgm|png|ppm|psd|tiff|tga|svg)", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            i++;
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                System.out.println("Image: " + strChcek + " - Code:" + checkCode);

                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    System.out.println("Image Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        System.out.println("Image Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("Image Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                        //End check missing file


                        //Check missing doc
                        pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(csv|doc|docx|djvu|odp|ods|odt|pps|ppsx|ppt|pptx|pdf|ps|eps|rtf|txt|wks|wps|xls|xlsx|xps)");
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                System.out.println("DOC: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    System.out.println("DOC Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        System.out.println("DOC Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("DOC Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                        // end check missing doc

                        //check missing ARCHIVES
                        pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)(/\\S*)\\.(7z|zip|rar|jar|tar|tar|gz|cab)", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                System.out.println("ARCHIRE: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    System.out.println("ARCHIVES Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        System.out.println("ARCHIVES Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("ARCHIVES Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                        //end check ARCHIVES

                        //check missing css
                        pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(css)(([\\?\\.\\=]\\w*)*)?", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
//            System.out.println(matcher.group());
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                System.out.println("CSS 2: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    System.out.println("CSS 2 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        System.out.println("CSS 2 Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("CSS 2 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                        //end check missing css

                        //check missing mp4 file
                        pattern = Pattern.compile("(http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?(/\\S*)\\.(3gp|avi|flv|m4v|mkv|mov|mp4|mpeg|ogv|wmv|webm)(\\w*)?", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                System.out.println("MP4: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    System.out.println("MP4 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        System.out.println("MP4 Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("MP4 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                        //end check mp4 file

                        //check missing mp3 file
                        pattern = Pattern.compile("(http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?(/\\S*)\\.(aac|ac3|aiff|amr|ape|flac|m4a|mka|mp3|mpc|ogg|wav|wma)", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                System.out.println("MP3: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    System.out.println("MP3 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        System.out.println("MP3 Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("MP3 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                        //end check Missing mp3 file
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                           });
            System.out.println(urlRoot);

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            t.join();
        }

        return missing;
    }


    // Function check http message Phúc Anh
    private String verifyHttpMessage(String url) {
        String message = "";
        try {
            URL urlTesst = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlTesst.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ");
            message = connection.getResponseMessage();
        } catch (Exception e) {
            message = "Not Found";
        }
        return message;
    }

    public List<CookieReport> cookieService(Url[] url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\trinhndse62136\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //Asign list JS info
        List<CookieReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
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
                            System.out.println("domain " + cookie.getDomain());
                            System.out.println("Name " + cookie.getName());
                            System.out.println("Path " + cookie.getPath());
                            System.out.println("Value " + cookie.getValue());
                            System.out.println("Version " + cookie.getExpiry());
                            System.out.println("");
                            resultList.add(new CookieReport(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getExpiry()));
                        }


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
        System.out.println("Size 1 " + resultList.size());
        Set<CookieReport> primesWithoutDuplicates = new LinkedHashSet<CookieReport>(resultList);

        // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet
        resultList.clear();

        // copying elements but without any duplicates
        resultList.addAll(primesWithoutDuplicates);
        System.out.println("Size 12 " + resultList.size());
        return resultList;

    }

}
