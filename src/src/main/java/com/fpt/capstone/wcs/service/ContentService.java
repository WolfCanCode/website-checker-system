package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.*;
import com.fpt.capstone.wcs.model.entity.ContactReport;
import com.fpt.capstone.wcs.model.entity.RedirectionReport;
import com.fpt.capstone.wcs.model.entity.Pages;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentService {
    public List<Pages> getPageInfor(Url[] list) throws IOException {
        List<Pages> pageCheck = new ArrayList<>();
        for (Url url:list){
            String title = getTitle(url.getUrl());
            int  httpcode = getStatus(url.getUrl());
            String canoUrl = getCanonicalUrl(url.getUrl());

            Pages page = new Pages(httpcode, url.getUrl(),title,canoUrl);
            pageCheck.add(page);
        }

        return pageCheck;
    }

    public  List<RedirectionReport> redirectionTest(Url[] list) throws IOException {
        List<RedirectionReport> pageCheck = new ArrayList<>();
        for(Url url:list){
            int  code =getStatus(url.getUrl());
            if(code== HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM){
                URL siteURL = new URL(url.getUrl());
                HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent","Mozilla/5.0 ");
                String message = connection.getResponseMessage();
                String newUrl = connection.getHeaderField("Location");

                // get the cookie if need, for lo
                // open the new connnection again
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
//                connection.setRequestProperty("CookieReport", cookies);
                connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                connection .addRequestProperty("User-Agent", "Mozilla");
                connection.addRequestProperty("Referer", "google.com");

                String codeNew = ""+code;
                RedirectionReport link = new RedirectionReport(code, url.getUrl(), message ,newUrl);
                pageCheck.add(link);
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

    public List<ContactReport> getContactDetail(Url[] list)throws IOException{
        List<ContactReport> list1 = new ArrayList<>();
        for (Url newList : list){
            try {
                String url = newList.getUrl();
                Document doc = Jsoup.connect(url).get();
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
//            System.out.println(matcher.group());
                    ContactReport phoneNumber = new ContactReport(matcher.group(),url,doc.getElementsContainingOwnText(matcher.group()).toString(),"Mail");
                    list1.add(phoneNumber);
                }
                // so my  ^\\(?(\\d{3,4})\\)?[-.\\s]?(\\d{3,4})[-.\\s]?(\\d{2,4})$
                // \\d{3,4}
            }catch (Exception ex){

            }
        }

//        Map<ContactDeatil, Integer> hm = new HashMap<ContactDeatil, Integer>();
//        for (ContactDeatil i : list1) {
//            Integer j = hm.get(i);
//            hm.put(i, (j == null) ? 1 : j + 1);
//        }
//
//
//
//        // displaying the occurrence of elements in the arraylist
//        for (Map.Entry<ContactDeatil, Integer> val : hm.entrySet()) {
//            System.out.println("Element " + val.getKey() + " "
//                    + "occurs"
//                    + ": " + val.getValue() + " times");
//        }
        return  list1;
    }



}
