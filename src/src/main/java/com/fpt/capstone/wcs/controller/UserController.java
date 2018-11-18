package com.fpt.capstone.wcs.controller;


import com.fpt.capstone.wcs.model.entity.Role;
import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.RoleRepository;
import com.fpt.capstone.wcs.repository.UserRepository;
import com.fpt.capstone.wcs.repository.WebsiteRepository;
import com.fpt.capstone.wcs.utils.Authenticate;
import com.fpt.capstone.wcs.utils.Constant;
import com.fpt.capstone.wcs.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

//email:admin@abc.com
//password:12345678 : hash ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f
@RestController
public class UserController {
    //scan repository
    final
    UserRepository userRepository;
    final
    Authenticate authenticate;
    final
    RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, Authenticate authenticate, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.authenticate = authenticate;
        this.roleRepository = roleRepository;
    }

    @CrossOrigin
    @PostMapping("/api/login")
    public Map<String, Object> doLogin(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        password = EncodeUtil.doEncode(password);
        User result = userRepository.findOneByEmailAndPasswordAndDelFlagEquals(email, password, false);
        Map<String, Object> res = new HashMap<>();
        if (result == null) {
            res.put("action", Constant.INCORRECT);
            res.put("messages", "The token is invalid");
            return res;
        } else {
            res.put("action", Constant.SUCCESS);
            res.put("id", result.getId());
            if (result.getManager() == null) {
                res.put("isManager", true);
            } else {
                res.put("isManager", false);
            }
            String token = EncodeUtil.generateAuthToken();
            res.put("token", token);
            result.setToken(token);
            userRepository.save(result);
            return res;
        }

    }

    @CrossOrigin
    @PostMapping("/api/auth")
    public Map<String, Object> isAuth(@RequestBody RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        User user = authenticate.isAuthGetSingleUser(request);
        if (user == null) {
            res.put("action", Constant.INCORRECT);
            res.put("messages", "The token is invalid");
            return res;
        } else {
            res.put("action", Constant.SUCCESS);
            String token = EncodeUtil.generateAuthToken();
            res.put("token", token);
            if (user.getManager() == null) {
                res.put("isManager", true);
            } else{
                res.put("isManager", false);
            }
            user.setToken(token);
            userRepository.save(user);
            return res;
        }

    }

}
