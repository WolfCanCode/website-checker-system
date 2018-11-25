package com.fpt.capstone.wcs.service.system.user;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.repository.user.RoleRepository;
import com.fpt.capstone.wcs.repository.user.UserRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateImpl;
import com.fpt.capstone.wcs.utils.Constant;
import com.fpt.capstone.wcs.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserImpl implements UserService{
    //scan repository
    final
    UserRepository userRepository;
    final
    AuthenticateImpl authenticate;
    final
    RoleRepository roleRepository;

    @Autowired
    public UserImpl(UserRepository userRepository, AuthenticateImpl authenticate, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.authenticate = authenticate;
        this.roleRepository = roleRepository;
    }

    @Override
    public Map<String, Object> isAuth(RequestCommonPOJO request) {
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

    @Override
    public Map<String, Object> doLogin(User user) {
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
}
