package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.LinkRedirection;
import com.fpt.capstone.wcs.model.Pages;
import com.fpt.capstone.wcs.model.SiteLink;
import com.fpt.capstone.wcs.model.Url;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public  List<LinkRedirection> redirectionTest(Url[] list) throws IOException {
        List<LinkRedirection> pageCheck = new ArrayList<>();
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
//                connection.setRequestProperty("Cookie", cookies);
                connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                connection .addRequestProperty("User-Agent", "Mozilla");
                connection.addRequestProperty("Referer", "google.com");

                String codeNew = ""+code;
                LinkRedirection link = new LinkRedirection(code, url.getUrl(), message ,newUrl);
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
//            connection.setConnectTimeout(500);
            code = connection.getResponseCode();
            String message = connection.getResponseMessage();
            if(code ==200){
                result =  code;
            }
            else if  (code == HttpURLConnection.HTTP_MOVED_PERM|| code== HttpURLConnection.HTTP_MOVED_TEMP){
                result = code;


                String newUrl = connection.getHeaderField("Location");

                // get the cookie if need, for login
//                String cookies = conn.getHeaderField("Set-Cookie");

                // open the new connnection again
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
//                connection.setRequestProperty("Cookie", cookies);
                connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                connection .addRequestProperty("User-Agent", "Mozilla");
                connection.addRequestProperty("Referer", "google.com");

                System.out.println("Address: "+url+" -Redirect to URL : " + newUrl+": Type: "+message);
            }
        } catch (Exception e) {
            result=404;
        }
        System.out.println(result);
        return result;
    }
}
