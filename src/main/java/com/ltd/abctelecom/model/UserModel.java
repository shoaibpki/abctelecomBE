package com.ltd.abctelecom.model;

import com.ltd.abctelecom.entity.Complaint;
import com.ltd.abctelecom.entity.Services;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserModel {

    private Long id;
    private String userName;
    private String email;
    private String password;
    private String mobile;
    private Role role;
    private String pinCode;
    private Set<Services> services;
    private List<Complaint> complaints;

}
