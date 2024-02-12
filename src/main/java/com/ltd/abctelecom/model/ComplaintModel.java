package com.ltd.abctelecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltd.abctelecom.entity.Services;
import com.ltd.abctelecom.entity.Users;
import lombok.*;

import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ComplaintModel {
    private Long complaintId;
    private String complaint;
    private String referenceNo;
    private ComplaintStatus status;
    private String feedback;
    private Long engineerId;
    private Instant cDate;
    private Instant jDate;
    private Instant rDate;
    private Users customer;
    private Services service;

}
