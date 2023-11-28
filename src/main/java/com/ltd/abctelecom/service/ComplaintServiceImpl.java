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
import org.springframework.beans.BeanUtils;
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

    @Override
    public List<ComplaintModel> getAllPendingComplains(String status) {
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
}
