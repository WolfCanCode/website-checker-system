package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.Pages;
import com.fpt.capstone.wcs.model.SiteLink;
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
    public List<Pages> getPageInfor(List<SiteLink> list) throws IOException {
        List<Pages> pageCheck = new ArrayList<>();
        for(SiteLink newList : list){

            String url =newList.getDesUrl();

            String title = getTitle(url);
            int  httoCode = getStatus(url);
            String canonicalUrl = getCanonicalUrl(url);

            Pages page = new Pages(url, title,canonicalUrl,httoCode);
            pageCheck.add(page);
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
