package com.fpt.capstone.wcs.controller;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.repository.UserRepository;
import com.fpt.capstone.wcs.repository.WebsiteRepository;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ConfigController {

    @Autowired
    WebsiteRepository websiteRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/website/all")
    public Map<String, Object> getAllWebsite4Test(@RequestBody User user) {
        Optional<User> result = userRepository.findById(user.getId());
        Map<String, Object> res = new HashMap<>();
        if (result!= null) {
            List<Website> websites = websiteRepository.findAllByUser(result.get());
            res.put("action", Constant.SUCCESS);
            res.put("website",websites);
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    

}
