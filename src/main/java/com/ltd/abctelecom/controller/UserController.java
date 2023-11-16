package com.ltd.abctelecom.controller;

import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("abctelecom/users")
@RestController
@Log4j2
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    ResponseEntity<UserModel> getUser(@RequestBody UserModel user){
        UserModel mUser = userService.getUser(user);
        log.info("Admin User: {}", mUser);
        return new ResponseEntity<>(mUser, HttpStatus.OK);
    }
}
