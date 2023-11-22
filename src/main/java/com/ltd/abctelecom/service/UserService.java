package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.model.UserModel;

import java.util.List;

public interface UserService {
    UserModel getUser(String email, String password);

    String createUser(UserModel user);

    List<UserModel> getAllUsers();

    UserModel updateUser(Long id, UserModel userModel);
}
