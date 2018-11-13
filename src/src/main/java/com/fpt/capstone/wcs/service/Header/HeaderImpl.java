package com.fpt.capstone.wcs.service.Header;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.UserRepository;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HeaderImpl implements HeaderInterFace {
    final
    UserRepository userRepository;
    final
    Authenticate authenticate;

    @Autowired
    public HeaderImpl(UserRepository userRepository, Authenticate authenticate) {
        this.userRepository = userRepository;
        this.authenticate = authenticate;
    }

    @Override
    public Map<String, Object> headerStaff(RequestCommonPOJO request) {
        List<Website> websites = authenticate.isAuthGetListSite(request);
        User user = userRepository.findById(request.getUserId()).get();
        Map<String, Object> res = new HashMap<>();
        if (websites != null) {
            res.put("action", Constant.SUCCESS);
            res.put("website", websites);
            res.put("fullname", user.getName());
            return res;
        } else {
            res.put("action", Constant.INCORRECT);
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
