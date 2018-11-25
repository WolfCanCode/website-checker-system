package com.fpt.capstone.wcs.service.system.authenticate;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticateImpl implements AuthenticateService{
    final
    UserRepository userRepository;
    final
    WebsiteRepository websiteRepository;

    @Autowired
    public AuthenticateImpl(UserRepository userRepository, WebsiteRepository websiteRepository) {
        this.userRepository = userRepository;
        this.websiteRepository = websiteRepository;
    }

    @Override
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

    @Override
    public WebsiteUserPOJO isAuthGetUserAndWebsite(RequestCommonPOJO request) {

        User user = userRepository.findOneByIdAndTokenAndDelFlagEquals(request.getUserId(),request.getUserToken(),false);
        if (user!=null) {
            Website website = websiteRepository.findOneByUserAndIdAndDelFlagEquals(user, request.getWebsiteId(),false);
            if (website != null) {
                WebsiteUserPOJO res = new WebsiteUserPOJO();
                res.setUser(user);
                res.setWebsite(website);
                return res;
            }
        }
        return null;
    }

    @Override
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

    @Override
    public User isAuthGetSingleUser(RequestCommonPOJO request)
    {
        User user = userRepository.findOneByIdAndTokenAndDelFlagEquals(request.getUserId(),request.getUserToken(),false);
        return  user;
    }

    @Override
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
