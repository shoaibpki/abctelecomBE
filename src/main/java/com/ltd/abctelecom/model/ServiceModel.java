package com.ltd.abctelecom.model;

import com.ltd.abctelecom.entity.Users;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceModel {

    private Long serviceId;
    private String name;
    private Set<Users> users;
}
