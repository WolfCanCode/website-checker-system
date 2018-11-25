package com.fpt.capstone.wcs.controller.system;


import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.user.RoleRepository;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.service.system.user.UserService;
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
    final
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/api/login")
    public Map<String, Object> doLogin(@RequestBody User user) {
        return userService.doLogin(user);
    }

    @CrossOrigin
    @PostMapping("/api/auth")
    public Map<String, Object> isAuth(@RequestBody RequestCommonPOJO request) {
        return userService.isAuth(request);
    }


}
