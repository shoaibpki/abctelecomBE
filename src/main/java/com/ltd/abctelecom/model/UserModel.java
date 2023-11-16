package com.ltd.abctelecom.model;

import com.ltd.abctelecom.entity.Services;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserModel {

    private Long userId;
    private String userName;
    private String email;
    private String password;
    private Role role;
    private String pinCode;
    private Set<Services> services;
}
