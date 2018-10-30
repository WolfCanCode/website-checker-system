package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.entity.ContactReport;
import com.fpt.capstone.wcs.model.entity.RedirectionReport;
import com.fpt.capstone.wcs.model.entity.PageReport;
import com.fpt.capstone.wcs.model.pojo.UrlPOJO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentService {
    public List<PageReport> getPageInfor(UrlPOJO[] list) throws InterruptedException {
        List<PageReport> pageCheck = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.length);
        List<Thread> listThread = new ArrayList<>();
        for (UrlPOJO url:list){
            listThread.add(new Thread(){
                public void run() {
                    try {
                        gate.await();
                        String title = getTitle(url.getUrl());
                        int  httpcode = getStatus(url.getUrl());
                        String canoUrl = getCanonicalUrl(url.getUrl());

                        PageReport page = new PageReport(httpcode, url.getUrl(),title,canoUrl);
                        pageCheck.add(page);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
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
        return pageCheck;
    }

    public  List<RedirectionReport> redirectionTest(UrlPOJO[] list) throws IOException {
        List<RedirectionReport> pageCheck = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.length);
        List<Thread> listThread = new ArrayList<>();
        for(UrlPOJO url:list){
            listThread.add(new Thread(){
                public void run() {
                    try {
                        int  code =getStatus(url.getUrl());
                        if(code== HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM){
                            URL siteURL = new URL(url.getUrl());
                            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("User-Agent","Mozilla/5.0 ");
                            String message = connection.getResponseMessage();
                            String newUrl = connection.getHeaderField("Location");
                            connection = (HttpURLConnection) new URL(newUrl).openConnection();
                            connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                            connection .addRequestProperty("User-Agent", "Mozilla");
                            connection.addRequestProperty("Referer", "google.com");
                            String codeNew = ""+code;
                            RedirectionReport link = new RedirectionReport(code, url.getUrl(), message ,newUrl);
                            pageCheck.add(link);
                        }
                    }catch (IOException e){
                        Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, e);
                    }

                }});

        }
        for (Thread t : listThread) {
            System.out.println("Threed start");
            t.start();
        }

        for (Thread t : listThread) {
            System.out.println("Threed join");
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return pageCheck;
    }

    public String getTitle(String url){
        String titleUrl="";
        try {
            Document doc = Jsoup.connect(url).get();
            titleUrl=doc.title();
            System.out.println(titleUrl);
        }catch (Exception ex){
        }
        return titleUrl;
    }


    public String getCanonicalUrl(String url){
        String canUrl ="";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements canoElements = doc.getElementsByTag("link");
            for(Element e: canoElements){
                String rel = e.attr("rel");
                if(rel.equals("canonical")){
                    canUrl = e.attr("href");
                }
            }
        }catch (Exception ex){
        }
        System.out.println(canUrl);
        return canUrl;
    }

    public int getStatus(String url) throws IOException {
        int  result = 0;
        int code = 200;
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 ");
            code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            if(code ==200){
                result =  code;
            }
            else if  (code == HttpURLConnection.HTTP_MOVED_PERM|| code== HttpURLConnection.HTTP_MOVED_TEMP){
                result = code;


                String newUrl = connection.getHeaderField("Location");
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                connection .addRequestProperty("User-Agent", "Mozilla");
                connection.addRequestProperty("Referer", "google.com");

                System.out.println("Addresses: "+url+" -Redirect to URL : " + newUrl+": Type: "+message);
            }
        } catch (Exception e) {
            result=404;
        }
        System.out.println(result);
        return result;

    }

    public List<ContactReport> getContactDetail(UrlPOJO[] list){
        List<ContactReport> list1 = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(list.length);
        List<Thread> listThread = new ArrayList<>();
        for (UrlPOJO newList : list){
            listThread.add(new Thread(){
                public void run() {
                    try {
                        String url = newList.getUrl();
                        Document doc = Jsoup.connect(url).ignoreContentType(true).get();
                        doc.body().text();
                        System.out.println("--------");
                        String patternPhone0 = "\\((\\d{3})\\)[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})";
                        String patternPhone1 = "\\d{4}[-.\\s]?\\d{3}[-.\\s]?\\d{3}";
                        String patternPhone2 = "\\(?\\d{3}\\)?([ ]\\d{1})?[ ]\\d{3,4}[ ]\\d{4}";
                        String patternPhone3 = "\\((\\d{4})\\)[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})";
                        String patternPhone4 = "[+]\\d{2}?[-.\\s]?\\d{2,3}?[-.\\s]?\\d{4}[-.\\s]?\\d{2,4}";
                        String patternPhone5 = "\\d{2}?[ ]?\\d{3}[ ]\\d{4}[ ]\\d{3,4}";
                        String patternPhone6 = "\\d{4}[.]\\d{4}[.]\\d{3}";
                        String patternPhone7 = "\\d{3}[.][ ]\\d{8}";
                        String patternPhone8 = "[+]\\d{2}[ ]\\(\\d{2}\\)[ ]\\d{1}[ ]\\d{3}[ ]\\d{4}";
                        String patternPhone9 = "\\d{3}-\\d{3}-\\d{4}";
                        String patternphone10 = "\\d{3}[ ]\\d{2}[ ]\\d{3}[ ]\\d{3}";
                        String patternPhone11 = "\\(\\d{3}\\)[ ]\\d{2}[ ]\\d{2}[ ]\\d{2}[ ]\\d{2}";
                        Pattern pattern = Pattern.compile(patternPhone0);
                        Matcher matcher = pattern.matcher(doc.wholeText());
                        while (matcher.find()) {
                            System.out.println("Phone0: " + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);

                        }

                        pattern = Pattern.compile(patternPhone1);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("Phone1: " + matcher.group() + " . " + matcher.start() + " " + matcher.end()+" ");
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }

                        pattern = Pattern.compile(patternPhone2);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 2:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }

                        pattern = Pattern.compile(patternPhone3);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 3:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }

                        pattern = Pattern.compile(patternPhone4);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 4:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }

                        pattern = Pattern.compile(patternPhone5);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 5:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }

                        pattern = Pattern.compile(patternPhone6);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 6:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }

                        pattern = Pattern.compile(patternPhone7);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 7:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }

                        pattern = Pattern.compile(patternPhone8);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 8:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }
                        pattern = Pattern.compile(patternPhone9);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 9:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }
                        pattern = Pattern.compile(patternphone10);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 10:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }
                        pattern = Pattern.compile(patternPhone11);
                        matcher = pattern.matcher(doc.wholeText());
                        // check all occurance
                        while (matcher.find()) {
                            System.out.println("PHONE 11:" + matcher.group());
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Phone");
                            list1.add(phoneNumber);
                        }
                        pattern = Pattern.compile("\\w+([.])?+\\w+@\\w+([-]\\w+)?[.]\\w+([.]\\w+)?+([.]\\w+)?");
                        matcher = pattern.matcher(doc.body().text());
                        while (matcher.find()) {
                            ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Mail");
                            list1.add(phoneNumber);
                        }
                        // so my  ^\\(?(\\d{3,4})\\)?[-.\\s]?(\\d{3,4})[-.\\s]?(\\d{2,4})$
                        // \\d{3,4}
                    }catch (Exception ex){
                        Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, ex);
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
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(ContentService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return  list1;
    }



}
