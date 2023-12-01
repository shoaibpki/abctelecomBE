package com.ltd.abctelecom.controller;

import com.ltd.abctelecom.entity.Services;
import com.ltd.abctelecom.model.ServiceModel;
import com.ltd.abctelecom.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("abctelecom/service")
public class ServiceController {

    @Autowired
    ServicesService servicesService;

    @PostMapping
    ResponseEntity<ServiceModel> createService(@RequestBody ServiceModel service){
        ServiceModel services = servicesService.CreateService(service);
        return new ResponseEntity<>(service, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<ServiceModel>> getAllService(){
        List<ServiceModel> serviceList = servicesService.getAllService();
        return ResponseEntity.ok(serviceList);
    }

    @GetMapping("{id}")
    ResponseEntity<ServiceModel> getServiceById(@PathVariable Long id){
        ServiceModel serviceModel = servicesService.getServiceById(id);

        return ResponseEntity.ok(serviceModel);
    }

    @PutMapping("{id}")
    ResponseEntity<ServiceModel> updateService(
            @PathVariable Long id,
            @RequestBody ServiceModel serviceModel){
        ServiceModel updateService = servicesService.updateService(id, serviceModel);

        return ResponseEntity.ok(updateService);
    }
}
