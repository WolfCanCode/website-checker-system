package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.*;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
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

    public List<MissingFilesPages> getMissingFile(Url[] url, String urlNew){
        List<MissingFilesPages> missing = new ArrayList<>();
        String urlRoot = urlNew;
        for (Url u: url){
            System.out.println(urlRoot);
            try {
                Document doc = Jsoup.connect(u.getUrl()).get();
                int i=0;
                //check missing image
                Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(jpg|gif|jp2|jpeg|pbm|pcx|pgm|png|ppm|psd|tiff|tga|svg)", Pattern.CASE_INSENSITIVE);
                Matcher matcher= pattern.matcher(doc.html());
                while (matcher.find()) {
                    i++;
                    String strChcek = matcher.group();
                    String checkCode = verifyHttpMessage(strChcek);
                    if(checkCode.equals("OK")){
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
                            System.out.println("Code Check Last: "+codeCheclast);
                            if (codeCheclast.equals("OK")) {
                                System.out.println("Image Last: " + checkLast + " - Code:" + codeCheclast);
                            }
                            else{
                                MissingFilesPages fileNew = new MissingFilesPages(strChcek, codeCheclast, u.getUrl());
                                missing.add(fileNew);
                                System.out.println("Image Last Fail: "+strChcek+" -Code: "+codeCheclast);
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
                    if(checkCode.equals("OK")){
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
                            System.out.println("Code Check Last: "+codeCheclast);
                            if (codeCheclast.equals("OK")) {
                                System.out.println("DOC Last: " + checkLast + " - Code:" + codeCheclast);
                            }
                            else{
                                MissingFilesPages fileNew = new MissingFilesPages(strChcek, codeCheclast, u.getUrl());
                                missing.add(fileNew);
                                System.out.println("DOC Last Fail: "+strChcek+" -Code: "+codeCheclast);
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
                    if(checkCode.equals("OK")){
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
                            System.out.println("Code Check Last: "+codeCheclast);
                            if (codeCheclast.equals("OK")) {
                                System.out.println("ARCHIVES Last: " + checkLast + " - Code:" + codeCheclast);
                            }
                            else{
                                MissingFilesPages fileNew = new MissingFilesPages(strChcek, codeCheclast, u.getUrl());
                                missing.add(fileNew);
                                System.out.println("ARCHIVES Last Fail: "+strChcek+" -Code: "+codeCheclast);
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
                    if(checkCode.equals("OK")){
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
                            System.out.println("Code Check Last: "+codeCheclast);
                            if (codeCheclast.equals("OK")) {
                                System.out.println("CSS 2 Last: " + checkLast + " - Code:" + codeCheclast);
                            }
                            else{
                                MissingFilesPages fileNew = new MissingFilesPages(strChcek, codeCheclast, u.getUrl());
                                missing.add(fileNew);
                                System.out.println("CSS 2 Last Fail: "+strChcek+" -Code: "+codeCheclast);
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
                    if(checkCode.equals("OK")){
                        System.out.println("MP4: " + strChcek + " - Code:" + checkCode);
                    }
                    if (!checkCode.equals("OK")) {
                        String checkAgain =  urlRoot + strChcek;
                        String codeCheckAgain = verifyHttpMessage(checkAgain);
                        if (codeCheckAgain.equals("OK")) {
                            System.out.println("MP4 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                        }
                        if (!codeCheckAgain.equals("OK")) {
                            String checkLast = "https:" + strChcek;
                            String codeCheclast = verifyHttpMessage(checkLast);
                            System.out.println("Code Check Last: "+codeCheclast);
                            if (codeCheclast.equals("OK")) {
                                System.out.println("MP4 Last: " + checkLast + " - Code:" + codeCheclast);
                            }
                            else{
                                MissingFilesPages fileNew = new MissingFilesPages(strChcek, codeCheclast, u.getUrl());
                                missing.add(fileNew);
                                System.out.println("MP4 Last Fail: "+strChcek+" -Code: "+codeCheclast);
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
                    if(checkCode.equals("OK")){
                        System.out.println("MP3: " + strChcek + " - Code:" + checkCode);
                    }
                    if (!checkCode.equals("OK")) {
                        String checkAgain =  urlRoot + strChcek;
                        String codeCheckAgain = verifyHttpMessage(checkAgain);
                        if (codeCheckAgain.equals("OK")) {
                            System.out.println("MP3 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                        }
                        if (!codeCheckAgain.equals("OK")) {
                            String checkLast = "https:" + strChcek;
                            String codeCheclast = verifyHttpMessage(checkLast);
                            System.out.println("Code Check Last: "+codeCheclast);
                            if (codeCheclast.equals("OK")) {
                                System.out.println("MP3 Last: " + checkLast + " - Code:" + codeCheclast);
                            }
                            else{
                                MissingFilesPages fileNew = new MissingFilesPages(strChcek, codeCheclast, u.getUrl());
                                missing.add(fileNew);
                                System.out.println("MP3 Last Fail: "+strChcek+" -Code: "+codeCheclast);
                            }
                        }
                    }
                }
                //end check Missing mp3 file
            } catch (IOException e) {
                Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return missing;
    }


    // Function check http message Phúc Anh
    private  String verifyHttpMessage(String url) {
        String message = "";
        try {
            URL urlTesst = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlTesst.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ");
            message = connection.getResponseMessage();
        } catch (Exception e) {
            message="Not Found";
        }
        return message;
    }

}
