package com.fpt.capstone.wcs.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckHTTPResponse {
    public static int verifyHttpMessage(String url) {
        int message = 0;
        try {
            URL urlTesst = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlTesst.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ");
            message = connection.getResponseCode();
        } catch (Exception e) {
            message = 404;
        }
        return message;
    }

    public static String getURLDirectsTo(String url) throws IOException {
        String urlDirectTo ="";
        int codeRespone = verifyHttpMessage(url);
        if( codeRespone>=300 && codeRespone<400){
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 ");
            int  message = connection.getResponseCode();
            String newUrl = connection.getHeaderField("Location");
            connection = (HttpURLConnection) new URL(newUrl).openConnection();
            connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            connection .addRequestProperty("User-Agent", "Mozilla");
            connection.addRequestProperty("Referer", "google.com");
            if(message<400 ||message>=500){
                urlDirectTo= newUrl;
            }

        }
        return urlDirectTo;
    }
}
