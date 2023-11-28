package com.ltd.abctelecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltd.abctelecom.entity.Users;
import lombok.*;

import javax.persistence.ManyToOne;
import java.time.Instant;

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
    private Long engineerId;
    private Instant cDate;
    private Instant rDate;
    private Users customer;

}
