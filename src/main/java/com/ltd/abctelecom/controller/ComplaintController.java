package com.ltd.abctelecom.controller;

import com.ltd.abctelecom.entity.Complaint;
import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.model.ComplaintModel;
import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("abctelecom")
public class ComplaintController {

    @Autowired
    ComplaintService complaintService;

    @PostMapping("user/{uid}/complaint")
    ResponseEntity<UserModel> createComplaint(
            @PathVariable Long uid,
            @RequestBody ComplaintModel complaintModel){
        UserModel user = complaintService.createComplaint(uid, complaintModel);
        return ResponseEntity.ok(user);
    }

    @GetMapping("complaint/{status}")
    ResponseEntity<List<ComplaintModel>> getAllPendingComplains(@PathVariable String status){
        List<ComplaintModel> complains = complaintService.getAllPendingComplains(status);
        return ResponseEntity.ok(complains);
    }



}
