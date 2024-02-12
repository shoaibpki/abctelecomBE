package com.ltd.abctelecom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;
    private String name;
    private String Description;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    },mappedBy = "services")
    private Set<Users> users = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "service", cascade = CascadeType.MERGE)
    private List<Complaint> complaints;
}
