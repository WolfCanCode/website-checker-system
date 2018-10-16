package com.fpt.capstone.wcs.service;

import com.fpt.capstone.wcs.model.BrokenLink;
import com.fpt.capstone.wcs.model.BrokenPage;
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
    public List<BrokenLink> brokenLinkService(Url[] url) throws InterruptedException {
        //Asign list Broken Link
        List<BrokenLink> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        HttpURLConnection connection = (HttpURLConnection) new URL(u.getUrl()).openConnection();
                        connection.connect();
                        String response = connection.getResponseMessage();
                        int respCode = connection.getResponseCode();
                        connection.disconnect();
                        System.out.println("URL: " + u.getUrl() + " returned " + response + " code " + respCode);
                        if(respCode == HttpURLConnection.HTTP_NOT_FOUND){

                            resultList.add(new BrokenLink(respCode, "https://www.nottingham.ac.uk/about", u.getUrl()));
                        }
                        //  resultList.add(new SpeedTest(u.url, interactTime1 + "", loadTime1 + "", sizeTransferred1 + ""));

                    }  catch (MalformedURLException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
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

    public List<BrokenPage> brokenPageService(Url[] url) throws InterruptedException {
        //Asign list Broken Page
        List<BrokenPage> resultList = new ArrayList<>();
        final CyclicBarrier gate = new CyclicBarrier(url.length);
        List<Thread> listThread = new ArrayList<>();

        for (Url u : url) {
            listThread.add(new Thread() {
                public void run() {
                    try {
                        gate.await();
                        HttpURLConnection connection = (HttpURLConnection) new URL(u.getUrl()).openConnection();
                        connection.connect();
                        String response = connection.getResponseMessage();
                        int respCode = connection.getResponseCode();
                        connection.disconnect();
                        System.out.println("URL: " + u.getUrl() + " returned " + response + " code " + respCode);
                        if(respCode == HttpURLConnection.HTTP_NOT_FOUND){

                            resultList.add(new BrokenPage(u.getUrl(), "Missing Page", respCode));
                        }

                        if(respCode == HttpURLConnection.HTTP_NO_CONTENT){

                            resultList.add(new BrokenPage(u.getUrl(), "Empty Page", respCode));
                        }


                    }  catch (MalformedURLException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
                    } catch (IOException e) {
                        Logger.getLogger(ExperienceService.class.getName()).log(Level.SEVERE, null, e);
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
