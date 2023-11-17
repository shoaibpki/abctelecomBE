package com.ltd.abctelecom.controller;

import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("abctelecom/users")
@RestController
@Log4j2
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    ResponseEntity<UserModel> getUser(@RequestParam String email, @RequestParam String password){
        UserModel mUser = userService.getUser(email, password);
        log.info("Admin User: {}", mUser);
        return new ResponseEntity<>(mUser, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> createUser(@RequestBody UserModel user){
        String msg = userService.creatUser(user);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<UserModel>> getAllUsers(){
        List<UserModel> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("{id}")
    ResponseEntity<UserModel> updateUser(@PathVariable Long id, @RequestBody UserModel userModel){
        UserModel user = userService.updateUser(id, userModel);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
