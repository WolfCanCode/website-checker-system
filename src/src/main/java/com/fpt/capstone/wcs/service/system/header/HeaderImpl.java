package com.fpt.capstone.wcs.service.system.header;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HeaderImpl implements HeaderService {
    final
    UserRepository userRepository;
    final
    AuthenticateImpl authenticate;

    @Autowired
    public HeaderImpl(UserRepository userRepository, AuthenticateImpl authenticate) {
        this.userRepository = userRepository;
        this.authenticate = authenticate;
    }

    @Override
    public Map<String, Object> headerStaff(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        List<Website> websites = authenticate.isAuthGetListSite(request);
        Optional<User> u = userRepository.findById(request.getUserId());
        if(u.isPresent()) {
            User user = u.get();
            if (websites != null) {
                res.put("action", Constant.SUCCESS);
                res.put("website", websites);
                res.put("fullname", user.getName());
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.SUCCESS);
            return res;

        }
    }

    @Override
    public Map<String, Object> headerManager(RequestCommonPOJO request) {
        List<Website> websites = authenticate.isAuthGetListSite(request);
        User user = userRepository.findById(request.getUserId()).get();
        Map<String, Object> res = new HashMap<>();
        if (websites != null) {
            res.put("action", Constant.SUCCESS);
            res.put("fullname", user.getName());
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }
}
