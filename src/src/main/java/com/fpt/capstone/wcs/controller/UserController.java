package com.fpt.capstone.wcs.controller;


import com.fpt.capstone.wcs.model.User;
import com.fpt.capstone.wcs.pojo.ResponsePojo;
import com.fpt.capstone.wcs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    //scan repository
    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/login")
    public User doLogin(@RequestBody User user)
    {
        String email = user.email;
        System.out.println(email);
        String password = user.password;
        User result = userRepository.findOneByEmailAndPassword(email,password);
        System.out.println(result);
        return result;
    }

    @GetMapping("/api/user")
    public List<User> test(){
        List<User> list = new ArrayList<>();
        list.add(new User("Truong","123","tr","tr"));
        list.add(new User("Trinh","123","tr","tr"));
        list.add(new User("Vi","123","tr","tr"));
        list.add(new User("P.A","123","tr","tr"));
        list.add(new User("Khanh","123","tr","tr"));

        return list;
    }


}
