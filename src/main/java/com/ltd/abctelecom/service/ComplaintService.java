package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Complaint;
import com.ltd.abctelecom.model.ComplaintModel;
import com.ltd.abctelecom.model.UserModel;

import java.util.List;

public interface ComplaintService {
    UserModel createComplaint(Long uid, ComplaintModel complaintModel);

    List<ComplaintModel> getAllComplaintsByStatus(String status);

    UserModel searchEngineerByPinCode(Long cid);

    ComplaintModel assignEngineerToComplaint(Long cid, Long eid);

    ComplaintModel resolvedComplaint(Long cid);

    List<Complaint> getComplaintsByEngineerId(Long uid);

    ComplaintModel jobNotDone(Long cid);
}
