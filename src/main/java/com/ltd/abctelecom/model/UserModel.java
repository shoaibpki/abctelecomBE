package com.ltd.abctelecom.model;

import com.ltd.abctelecom.entity.Services;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Long serviceId;
    private String userName;
    private String email;
    private String password;
    private Role role;
    private Set<Services> services;
}
