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

import java.util.List;
import java.util.Optional;

@Service
public class Authenticate {
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebsiteRepository websiteRepository;

    public Website isAuth(RequestCommonPOJO request) {

        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isPresent()) {
            Website website = websiteRepository.findOneByUserAndId(user.get(), request.getWebsiteId());
            if (website != null) {
                return website;
            }
        }
        return null;
    }

    public List<Website> isAuthList(RequestCommonPOJO request) {

        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isPresent()) {
            List<Website> website = websiteRepository.findAllByUser(user.get());
            if (website != null) {
                return website;
            }
        }
        return null;
    }

}
