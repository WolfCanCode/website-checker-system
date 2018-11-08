package com.fpt.capstone.wcs.utils;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
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

    public Website isAuthGetSingleSite(RequestCommonPOJO request) {

        User user = userRepository.findOneByIdAndTokenAndDelFlagEquals(request.getUserId(),request.getUserToken(),false);
        if (user!=null) {
            Website website = websiteRepository.findOneByUserAndIdAndDelFlagEquals(user, request.getWebsiteId(),false);
            if (website != null) {
                return website;
            }
        }
        return null;
    }

    public List<Website> isAuthGetListSite(RequestCommonPOJO request) {

        User user = userRepository.findOneByIdAndTokenAndDelFlagEquals(request.getUserId(),request.getUserToken(),false);
        if (user!=null) {
            List<Website> website = websiteRepository.findAllByUserAndDelFlagEquals(user,false);
            if (website != null) {
                return website;
            }
        }
        return null;
    }

    public User isAuthGetSingleUser(RequestCommonPOJO request)
    {
        User user = userRepository.findOneByIdAndTokenAndDelFlagEquals(request.getUserId(),request.getUserToken(),false);
        return  user;
    }

    public User isAuthAndManagerGet(ManagerRequestPOJO request)
    {
        User user = userRepository.findOneByIdAndTokenAndDelFlagEquals(request.getManagerId(),request.getManagerToken(),false);
        if(user.getManager()==null)
        {
            return  user;
        } else {
            return null;
        }
    }


}
