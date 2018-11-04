package com.fpt.capstone.wcs.utils;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.UserRepository;
import com.fpt.capstone.wcs.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class Authenticate {
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebsiteRepository websiteRepository;

    public Website isAuth(RequestCommonPOJO request) {

        User user = userRepository.findOneByIdAndToken(request.getUserId(), request.getUserToken());
        if (user != null) {
            Website website = websiteRepository.findOneByUserAndId(user, request.getWebsiteId());
            if (website != null) {
                return website;
            }
        }
        return null;
    }

}
