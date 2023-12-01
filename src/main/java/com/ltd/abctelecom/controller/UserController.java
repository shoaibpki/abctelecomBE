package com.ltd.abctelecom.controller;

import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.model.ServiceModel;
import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RequestMapping("abctelecom/users")
@RestController
@Log4j2
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    ResponseEntity<UserModel> getUser(@RequestParam String email, @RequestParam String password){
        UserModel mUser = userService.getUser(email, password);
        log.info("Admin User: {}", mUser);
        return new ResponseEntity<>(mUser, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> createUser(@RequestBody UserModel user){
        String msg = userService.createUser(user);
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

    @PostMapping("{uid}/service/{sid}")
    ResponseEntity<UserModel> assignServiceToCustomer(
            @PathVariable Long uid,
            @PathVariable Long sid){
        UserModel userModel =
                userService.assignServiceToCustomer(uid, sid);
        return ResponseEntity.ok(userModel);
    }

    @DeleteMapping("{uid}/service/{sid}")
    ResponseEntity<UserModel> deleteServiceFromUser(
            @PathVariable Long uid,
            @PathVariable Long sid){
        UserModel userModel =
                userService.deleteServiceFromUser(uid, sid);
        return ResponseEntity.ok(userModel);

    }

}
