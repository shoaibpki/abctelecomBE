package com.ltd.abctelecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long engineerId;
    private Instant cDate;
    private Instant jDate;
    private Instant rDate;

    @JsonIgnore
    @ManyToOne
    private Users customer;
}
