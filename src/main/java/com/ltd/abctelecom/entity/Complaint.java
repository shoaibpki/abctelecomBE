package com.ltd.abctelecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintId;
    private String complaint;
    private String referenceNo;
    private String status;
    private String feedback;

    private Long engineerId;
    private Instant cDate; // complaint generated date
    private Instant jDate; // complaint assigning to engineer date
    private Instant rDate; // complaint resolved date
    @JsonIgnore
    @ManyToOne
    private Users customer;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "service_id", referencedColumnName = "serviceId")
    private Services service;
}
