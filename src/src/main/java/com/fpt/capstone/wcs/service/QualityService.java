package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.entity.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.BrokenPageReport;
import com.fpt.capstone.wcs.model.Url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QualityService {
    public List<BrokenLinkReport> brokenLinkService(Url[] url) throws InterruptedException {
        //Asign list Broken Link
        List<BrokenLinkReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        HttpURLConnection connection = (HttpURLConnection) new URL(u.getUrl()).openConnection();
                        connection.connect();
                        String responseMessage = connection.getResponseMessage();
                        int responseCode = connection.getResponseCode();
                        connection.disconnect();
                        System.out.println("URL: " + u.getUrl() + " returned " + responseMessage + " code " + responseCode);
                        if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                            resultList.add(new BrokenLinkReport(responseCode, responseMessage,"https://www.nottingham.ac.uk/about", u.getUrl()));


                        }

                        if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                            resultList.add(new BrokenLinkReport(responseCode, responseMessage,"https://www.nottingham.ac.uk/about", u.getUrl()));


                        }


                    }  catch (MalformedURLException e) {

                        System.out.println("hihi 1");
                        System.out.println("message 1" + e.getMessage());
                        if(e.toString().contains("java.net.MalformedURLException")){
                            resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request","https://www.nottingham.ac.uk/about", u.getUrl()));
                        }
                       // Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("hihi 2");
                        System.out.println("message 2" + e.getMessage());
                        System.out.println("message 2.1" + e.toString());
                        if(e.toString().contains("java.net.UnknownHostException")){
                            resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request","https://www.nottingham.ac.uk/about", u.getUrl()));
                        }

//                        if(e.toString().contains("java.net.ConnectException")){
//                            resultList.add(new BrokenLinkReport(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "Gateway Timeout","https://www.nottingham.ac.uk/about", u.getUrl()));
//                        }

                        //Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (InterruptedException e) {
                        System.out.println("hihi 3");
                        System.out.println("message 3" + e.getMessage());
                        //Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
                        System.out.println("hihi 4");
                        System.out.println("message 4" + e.getMessage());
                       // Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
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

    public List<BrokenPageReport> brokenPageService(Url[] url) throws InterruptedException {
        //Asign list Broken Page
        List<BrokenPageReport> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        HttpURLConnection connection = (HttpURLConnection) new URL(u.getUrl()).openConnection();
                        connection.connect();
                        String responseMessage = connection.getResponseMessage();
                        int responseCode = connection.getResponseCode();
                        connection.disconnect();
                        int[] errResponseCode = {203 ,204 , 205 , 206, 403 ,400,  405 , 409 , 500 , 511, 503 , 505, 507, 510 , 511 };

                        System.out.println("URL: " + u.getUrl() + " returned " + responseMessage + " code " + responseCode);
                        if(responseCode == 404){

                            resultList.add(new BrokenPageReport(u.getUrl(), "Missing Page", responseCode , responseMessage));
                        }

                        for (int i = 0; i < errResponseCode.length; i++){
                            if(responseCode == errResponseCode[i] ){
                                resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", responseCode , responseMessage));

                            }

                        }




                    }  catch (MalformedURLException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                        if(e.toString().contains("java.net.MalformedURLException")){
                            resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request"));
                        }
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                        if(e.toString().contains("java.net.UnknownHostException")){
                            resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_BAD_REQUEST , "Bad Request"));
                        }


                        if(e.toString().contains("java.net.ConnectException")){
                            resultList.add(new BrokenPageReport(u.getUrl(), "Error Page", HttpURLConnection.HTTP_GATEWAY_TIMEOUT , "Gateway Timeout"));
                        }
                    } catch (InterruptedException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (BrokenBarrierException e) {
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
