package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.*;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import com.fpt.capstone.wcs.service.Experience.ExperienceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import sun.misc.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QualityService {



    public List<MissingFileReport> getMissingFileImg(UrlPOJO[] url, String urlNew) throws InterruptedException {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (UrlPOJO u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(jpg|gif|jp2|jpeg|png|psd|tga|svg)", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            i++;
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("Image: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("Image Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
                                        System.out.println("Image Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("Image Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
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

    public List<MissingFileReport> getMissingFileDoc(UrlPOJO[] url, String urlNew) throws InterruptedException {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (UrlPOJO u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(doc|docx|ppt|pptx|pdf|ps|txt|xls|xlsx)");
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("DOC: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("DOC Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
                                        System.out.println("DOC Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("DOC Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
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

    public List<MissingFileReport> getMissingFileCss(UrlPOJO[] url, String urlNew) throws InterruptedException {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (UrlPOJO u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(css)(([\\?\\.\\=]\\w*)*)?", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
//            System.out.println(matcher.group());
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("CSS 2: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("CSS 2 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("CSS 2 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("CSS 2 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
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

    public List<MissingFileReport> getMissingFileMP3andMP4(UrlPOJO[] url, String urlNew) throws InterruptedException {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (UrlPOJO u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("(http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?(/\\S*)\\.(mp3|avi|flv|mp4)(\\w*)?", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                System.out.println("MP4: " + strChcek + " - Code:" + checkCode);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    System.out.println("MP4 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport( checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("MP4 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport( checkLast,  codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("MP4 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
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

    public List<MissingFileReport> getMissingFileARCHIVES(UrlPOJO[] url, String urlNew) throws InterruptedException {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (UrlPOJO u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        Document doc = Jsoup.connect(u.getUrl()).get();
                        int i = 0;
                        //check missing image
                        Pattern pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)(/\\S*)\\.(7z|zip|rar|jar|tar|tar|gz|cab)", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("ARCHIRE: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("ARCHIVES Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
                                        System.out.println("ARCHIVES Last: " + checkLast + " - Code:" + codeCheclast);
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("ARCHIVES Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
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

    public List<MissingFileReport> getMissingFile(UrlPOJO[] url, String urlNew) throws InterruptedException {
        List<MissingFileReport> missing = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();
        String urlRoot = urlNew;
        for (UrlPOJO u : url) {
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
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("Image: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("Image Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
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
                        pattern = Pattern.compile("((http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?)?(/\\S*)\\.(doc|docx|djvu|odp|ods|odt|pps|ppsx|ppt|pptx|pdf|ps|eps|rtf|txt|wks|wps|xls|xlsx|xps)");
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("DOC: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("DOC Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
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
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("ARCHIRE: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("ARCHIVES Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
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
                                byte[] capacity =  getBytes(strChcek);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                                System.out.println("CSS 2: " + strChcek + " - Code:" + checkCode);
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                    System.out.println("CSS 2 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("CSS 2 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
                                    } else {
                                        MissingFileReport fileNew = new MissingFileReport(strChcek, codeCheclast, u.getUrl());
                                        missing.add(fileNew);
                                        System.out.println("CSS 2 Last Fail: " + strChcek + " -Code: " + codeCheclast);
                                    }
                                }
                            }
                        }
                        //end check missing css
                        // check missing file js

                        //end check missing file js
                        //check missing mp4 file
                        pattern = Pattern.compile("(http\\:|https\\:)?//?([\\w\\-?\\.?]+)?\\.([a-zA-Z]{2,3})?(/\\S*)\\.(3gp|avi|flv|m4v|mkv|mov|mp4|mpeg|ogv|wmv|webm)(\\w*)?", Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(doc.html());
                        while (matcher.find()) {
                            String strChcek = matcher.group();
                            String checkCode = verifyHttpMessage(strChcek);
                            if (checkCode.equals("OK")) {
                                byte[] capacity =  getBytes(strChcek);
                                System.out.println("MP4: " + strChcek + " - Code:" + checkCode);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    System.out.println("MP4 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport( checkAgain, codeCheckAgain+" size: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        System.out.println("MP4 Last: " + checkLast + " - Code:" + codeCheclast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport( checkLast,  codeCheclast+" size: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
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
                                byte[] capacity =  getBytes(strChcek);
                                System.out.println("MP3: " + strChcek + " - Code:" + checkCode);
                                if(capacity.length==0){
                                    MissingFileReport fileNew = new MissingFileReport(strChcek, checkCode+" size:: "+capacity.length, u.getUrl());
                                    missing.add(fileNew);
                                }
                            }
                            if (!checkCode.equals("OK")) {
                                String checkAgain = urlRoot + strChcek;
                                String codeCheckAgain = verifyHttpMessage(checkAgain);
                                if (codeCheckAgain.equals("OK")) {
                                    byte[] capacity =  getBytes(checkAgain);
                                    System.out.println("MP3 Again: " + checkAgain + " - Code:" + codeCheckAgain);
                                    if(capacity.length==0){
                                        MissingFileReport fileNew = new MissingFileReport(checkAgain, codeCheckAgain+" size:: "+capacity.length, u.getUrl());
                                        missing.add(fileNew);
                                    }
                                }
                                if (!codeCheckAgain.equals("OK")) {
                                    String checkLast = "https:" + strChcek;
                                    String codeCheclast = verifyHttpMessage(checkLast);
                                    System.out.println("Code Check Last: " + codeCheclast);
                                    if (codeCheclast.equals("OK")) {
                                        byte[] capacity =  getBytes(checkLast);
                                        if(capacity.length==0){
                                            MissingFileReport fileNew = new MissingFileReport(checkLast, codeCheclast+" size:: "+capacity.length, u.getUrl());
                                            missing.add(fileNew);
                                        }
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
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        Logger.getLogger(ExperienceImpl.class.getName()).log(Level.SEVERE, null, e);
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

    //Function get size file
    private  byte[] getBytes(String url)  {
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
        }catch (IOException ex){
            
        }
        
        return b;
    }

}
