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
    public ComplaintModel createComplaint(Long uid, ComplaintModel complaintModel) {

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
                    .service(complaintModel.getService())
                    .build();
            log.info("complaint's values : {}",complaint);
            complaintRepository.save(complaint);
            log.info("Successfully launch a Complaint: {}", complaint);
            copyProperties(complaint, complaintModel);
            return complaintModel;
        }else {
            throw new CustomException(
                    "Customer can launch a Complaint",
                    "NOT_AUTHORISED");
        }
    }
    @Override
    public List<ComplaintModel> getAllComplaints() {
        List<Complaint> complains = complaintRepository.findAll();
        log.info("::{}", complains);
        return complains.stream().map( complaint ->
                ComplaintModel.builder()
                        .complaintId(complaint.getComplaintId())
                        .complaint(complaint.getComplaint())
                        .referenceNo(complaint.getReferenceNo())
                        .cDate(complaint.getCDate())
                        .jDate(complaint.getJDate())
                        .customer(complaint.getCustomer())
                        .feedback(complaint.getFeedback())
                        .service(complaint.getService())
                        .engineerId(complaint.getEngineerId())
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
            return UserModel.builder()
                    .id(engineer.getId())
                    .userName(engineer.getUserName())
                    .email(engineer.getEmail())
                    .pinCode(engineer.getPinCode())
                    .role(Role.valueOf(engineer.getRole()))
                    .build();
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

            return ComplaintModel.builder()
                    .engineerId(complaint.getEngineerId())
                    .complaintId(complaint.getComplaintId())
                    .complaint(complaint.getComplaint())
                    .jDate(complaint.getJDate())
                    .referenceNo(complaint.getReferenceNo())
                    .feedback(complaint.getFeedback())
                    .service(complaint.getService())
                    .customer(complaint.getCustomer())
                    .status(ComplaintStatus.valueOf(complaint.getStatus()))
                    .cDate(complaint.getCDate())
                    .build();

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

        return ComplaintModel.builder()
                .engineerId(complaint.getEngineerId())
                .complaintId(complaint.getComplaintId())
                .complaint(complaint.getComplaint())
                .jDate(complaint.getJDate())
                .referenceNo(complaint.getReferenceNo())
                .feedback(complaint.getFeedback())
                .status(ComplaintStatus.valueOf(complaint.getStatus()))
                .cDate(complaint.getCDate())
                .rDate(complaint.getRDate())
                .customer(complaint.getCustomer())
                .build();
    }

    @Override
    public List<Complaint> getComplaintsByEngineerId(Long uid) {
        try{

            return complaintRepository.findByEngineerIdAndStatus(uid, "ON_GOING");
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
        return ComplaintModel.builder()
                .complaintId(complaint.getComplaintId())
                .referenceNo(complaint.getReferenceNo())
                .complaint(complaint.getComplaint())
                .jDate(complaint.getJDate())
                .engineerId(complaint.getEngineerId())
                .feedback(complaint.getFeedback())
                .cDate(complaint.getCDate())
                .status(ComplaintStatus.valueOf(complaint.getStatus()))
                .rDate(complaint.getRDate())
                .customer(complaint.getCustomer())
                .build();
    }

    @Override
    public ComplaintModel saveFeedback(ComplaintModel comp) {
        long cid = comp.getComplaintId();
        Complaint complaint = complaintRepository.findById(cid)
                .orElseThrow(()-> new CustomException("",""));
        complaint.setFeedback(comp.getFeedback());
        complaintRepository.save(complaint);
        copyProperties(complaint, comp);
        return comp;
    }
}
