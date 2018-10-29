package com.fpt.capstone.wcs.controller;


import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.repository.UserRepository;
import com.fpt.capstone.wcs.utils.Constant;
import com.fpt.capstone.wcs.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//email:admin@abc.com
//password:12345678 : hash ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f
@RestController
public class UserController {
    //scan repository
    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/login")
    public Map<String, Object> doLogin(@RequestBody User user)
    {
        String email = user.getEmail();
        System.out.println(email);
        String password = user.getPassword();
        password = new EncodeUtil().doEncode(password);
        User result = userRepository.findOneByEmailAndPassword(email,password);
        Map<String, Object> res = new HashMap<>();
        if(result==null)
        {
            res.put("action", Constant.INCORRECT);
            return res;
        } else {
            res.put("action", Constant.SUCCESS);
            res.put("id", result.getId());
            String token = new EncodeUtil().generateAuthToken();
            res.put("token", token);
            result.setToken(token);
            userRepository.save(result);
            return res;
        }

    }

    @PostMapping("/api/auth")
    public Map<String, Object> isAuth(@RequestBody User user)
    {
        User result = userRepository.findOneByIdAndToken(user.getId(),user.getToken());
        Map<String, Object> res = new HashMap<>();
        if(result==null)
        {
            res.put("action", Constant.INCORRECT);
            return res;
        } else {
            res.put("action", Constant.SUCCESS);
            String token = new EncodeUtil().generateAuthToken();
            res.put("token", token);
            result.setToken(token);
            userRepository.save(result);
            return res;
        }

    }

    @PostMapping("/api/user/name")
    public Map<String, Object> getName(@RequestBody User user)
    {
        Optional<User> result = userRepository.findById(user.getId());
        Map<String, Object> res = new HashMap<>();
        if(result==null)
        {
            res.put("action", Constant.INCORRECT);
            return res;
        } else {
            res.put("action", Constant.SUCCESS);
            res.put("name", result.get().getName());
            return res;
        }

    }

}
