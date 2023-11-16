package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.exception.CustomException;
import com.ltd.abctelecom.model.Role;
import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel getUser(String email, String password) {
//        checking super user admin
        if ( email.equalsIgnoreCase ("admin@abc.com") &&
                password.equalsIgnoreCase("123")){
//            set admin user default properties
            log.info("email: {}, password: {}",email, password);
            UserModel mUser = UserModel.builder()
                    .userName("Admin")
                    .role(Role.ADMIN)
                    .email(email)
                    .build();
            return mUser;
        }else { // checking other user for login
            try {
                Users user = userRepository.findByEmailAndPassword(email, password);
                UserModel muser = UserModel.builder()
                        .email(user.getEmail())
                        .role(Role.valueOf(user.getRole()))
                        .userName(user.getUserName())
                        .build();
                return muser;

            }catch (Exception e){
                throw new CustomException(
                        "User not found by given email or entered wrong passowrd",
                        "USER_NOT_FOUND"
                        );
            }
        }
    }

    @Override
    public String creatUser(UserModel user) {
        log.info("Adding User..: {}",user);
        Users muser = Users.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
        userRepository.save(muser);
        return "Successfully Created user with id : "+ muser.getUserId();
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream().map( user ->
                UserModel.builder()
                        .userId(user.getUserId())
                        .services(user.getServices())
                        .pinCode(user.getPinCode())
                        .role(Role.valueOf(user.getRole()))
                        .userName(user.getUserName())
                        .email(user.getEmail())
                        .build()

        ).collect(Collectors.toList());
    }
}
