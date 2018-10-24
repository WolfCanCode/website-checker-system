package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConfigController {
    @Autowired
    WebsiteRepository websiteRepository;

    @GetMapping("/api/website/all")
    public List<Website> getAllWebsite4Test()
    {
        List<Website> websites = websiteRepository.findAll();
        return  websites;
    }

}
