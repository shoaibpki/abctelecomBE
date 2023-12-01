package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Complaint;
import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.exception.CustomException;
import com.ltd.abctelecom.model.ComplaintModel;
import com.ltd.abctelecom.model.ComplaintStatus;
import com.ltd.abctelecom.model.Role;
import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.repository.ComplaintRepository;
import com.ltd.abctelecom.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    ComplaintRepository complaintRepository;

    @Autowired
    UserRepository userRepository;
    @Override
    public UserModel createComplaint(Long uid, ComplaintModel complaintModel) {

        log.info("::{}", complaintModel);
        Format f = new SimpleDateFormat("dd/MMMM/yyyy");
        f = new SimpleDateFormat("M"); // get current month formate
        String strMonth = f.format(new Date()); // get string month in digits
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new CustomException(
                        "User not found by given id: {}"+ uid,
                        "USER_NOT_FOUND"
                ));
        if (user.getRole().equalsIgnoreCase("CUSTOMER")){
            log.info("Adding complaint against user id: {}", uid);
            Complaint complaint = Complaint.builder()
                    .complaint(complaintModel.getComplaint())
                    .referenceNo(UUID.randomUUID().toString() + "-" + strMonth)
                    .cDate(Instant.now())
                    .status(complaintModel.getStatus().name())
                    .customer(user)
                    .build();
            complaintRepository.save(complaint);
            log.info("Successfully launch a Complaint: {}", complaint);
            UserModel userModel = UserModel.builder()
                    .id(user.getId())
                    .userName(user.getUserName())
                    .email(user.getEmail())
                    .role(Role.valueOf(user.getRole()))
                    .pinCode(user.getPinCode())
                    .services(user.getServices())
                    .complaints(user.getComplaints())
                    .build();
            return userModel;
        }else {
            throw new CustomException(
                    "Customer can launch a Complaint",
                    "NOT_AUTHORISED");
        }
    }

    public List<ComplaintModel> getAllComplaintsByStatus(String status) {
        List<Complaint> complains = complaintRepository
                .findByStatus(status.toUpperCase(Locale.ROOT));
        log.info("::{}", complains);
        return complains.stream().map( complaint ->
                ComplaintModel.builder()
                        .complaintId(complaint.getComplaintId())
                        .complaint(complaint.getComplaint())
                        .referenceNo(complaint.getReferenceNo())
                        .cDate(complaint.getCDate())
                        .customer(complaint.getCustomer())
                        .status(ComplaintStatus.valueOf(complaint.getStatus()))
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public UserModel searchEngineerByPinCode(Long cid) {
        Complaint complaint = complaintRepository.findById(cid)
                .orElseThrow(() -> new CustomException(
                        "Wrong complaint id is given",
                        "NOT_FOUND"
                ));
        try {
            Users user = complaint.getCustomer();
            Users engineer =
                    userRepository.findByPinCodeAndRole(user.getPinCode(),"ENGINEER");
            UserModel userModel = UserModel.builder()
                    .id(engineer.getId())
                    .userName(engineer.getUserName())
                    .email(engineer.getEmail())
                    .pinCode(engineer.getPinCode())
                    .role(Role.valueOf(engineer.getRole()))
                    .build();
            return userModel;
        }catch (Exception e){
            throw new CustomException(
                    "Engineer not available in this area for a while",
                    "NOT_FOUND");
        }
    }

    @Override
    public ComplaintModel assignEngineerToComplaint(Long cid, Long eid) {
        Complaint complaint = complaintRepository.findById(cid)
                .orElseThrow(()-> new CustomException(
                        "Complaint not Found by given id: "+cid,
                        "NOT_FOUND"));

        if (userRepository.existsByIdAndRole(eid, "ENGINEER")){
            complaint.setJDate(Instant.now());
            complaint.setStatus("ON_GOING");
            complaint.setEngineerId(eid);
            complaintRepository.save(complaint);

            ComplaintModel complaintModel = ComplaintModel.builder()
                    .engineerId(complaint.getEngineerId())
                    .complaintId(complaint.getComplaintId())
                    .complaint(complaint.getComplaint())
                    .jDate(complaint.getJDate())
                    .referenceNo(complaint.getReferenceNo())
                    .status(ComplaintStatus.valueOf(complaint.getStatus()))
                    .cDate(complaint.getCDate())
                    .build();
            return complaintModel;

        }else {
            throw new CustomException(
                    "Not the Engineer by given id: "+eid,
                    "NOT_AUTHORISED");
        }
    }

    @Override
    public ComplaintModel resolvedComplaint(Long cid) {
        Complaint complaint = complaintRepository.findById(cid)
                .orElseThrow(()-> new CustomException(
                        "Complaint not Found by given id: "+cid,
                        "NOT_FOUND"));
            complaint.setStatus("RESOLVED");
            complaint.setRDate(Instant.now());
            complaintRepository.save(complaint);

            ComplaintModel complaintModel = ComplaintModel.builder()
                    .engineerId(complaint.getEngineerId())
                    .complaintId(complaint.getComplaintId())
                    .complaint(complaint.getComplaint())
                    .jDate(complaint.getJDate())
                    .referenceNo(complaint.getReferenceNo())
                    .status(ComplaintStatus.valueOf(complaint.getStatus()))
                    .cDate(complaint.getCDate())
                    .rDate(complaint.getRDate())
                    .customer(complaint.getCustomer())
                    .build();
            return complaintModel;
    }

    @Override
    public List<Complaint> getComplaintsByEngineerId(Long uid) {
        try{
            List<Complaint> complaints =
                    complaintRepository.findByEngineerIdAndStatus(uid, "ON_GOING");

            return complaints;
        }catch (Exception e){
            throw new CustomException(
                    "Engineer not Found by given Id"+ uid,
                    "NOT_FOUND");
        }
    }

    @Override
    public ComplaintModel jobNotDone(Long cid) {
        Complaint complaint = complaintRepository.findById(cid)
                .orElseThrow(()-> new CustomException("",""));
        complaint.setRDate(null);
        complaint.setStatus("ESCALATED");
        complaintRepository.save(complaint);
        ComplaintModel complaintModel = ComplaintModel.builder()
                .complaintId(complaint.getComplaintId())
                .referenceNo(complaint.getReferenceNo())
                .complaint(complaint.getComplaint())
                .jDate(complaint.getJDate())
                .engineerId(complaint.getEngineerId())
                .cDate(complaint.getCDate())
                .status(ComplaintStatus.valueOf(complaint.getStatus()))
                .rDate(complaint.getRDate())
                .customer(complaint.getCustomer())
                .build();
        return complaintModel;
    }
}
