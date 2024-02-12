package com.ltd.abctelecom.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private String mobile;
    private String email;
    private String role;
    private String pinCode;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "customer_service",
            joinColumns = {@JoinColumn(name = "customer_id")},
            inverseJoinColumns = {@JoinColumn(name = "service_id")}
    )
    private Set<Services> services = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private List<Complaint> complaints = new ArrayList<>();

}
