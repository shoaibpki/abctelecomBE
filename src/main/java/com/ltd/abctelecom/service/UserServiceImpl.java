package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.model.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserServiceImpl implements UserService{
    @Override
    public UserModel getUser(UserModel user) {
//        cheking super user admin
        if (user.getEmail().equalsIgnoreCase("admin@abc.com") &&
                user.getPassword().equalsIgnoreCase("123")){
            log.info("{}",user);
            return user;
        }
        return null;
    }
}
