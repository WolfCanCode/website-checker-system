package com.fpt.capstone.wcs.test;

import com.fpt.capstone.wcs.service.SiteMapService;

import java.net.MalformedURLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        SiteMapService sms  = new SiteMapService("https://vnexpress.net");
        sms.buildSiteMap();
//        sms.makeUI(); // render hierarchy order


        System.out.println("==========Internal links========== ");
        List<String> internalsLinks = sms.getInternalLink();
        for (String url : internalsLinks){
            System.out.println(url);
        }

        System.out.println("==============END==============");


        System.out.println("==========All links========== ");
        List<String> allLink = sms.getAllLinks();
        for (String url : allLink){
            System.out.println(url);
        }

        System.out.println("==============END==============");
    }
}
