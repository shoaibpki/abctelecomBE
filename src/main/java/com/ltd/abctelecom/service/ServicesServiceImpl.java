package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Services;
import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.exception.CustomException;
import com.ltd.abctelecom.model.ServiceModel;
import com.ltd.abctelecom.repository.ServiceRepository;
import com.ltd.abctelecom.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ServicesServiceImpl implements ServicesService{

    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public ServiceModel CreateService(ServiceModel service) {
        Services services = Services.builder()
                .name(service.getName())
                .Description(service.getDescription())
                .build();
        serviceRepository.save(services);
        copyProperties(services, service);
        return service;
    }

    @Override
    public List<ServiceModel> getAllService() {
        List<Services> services = serviceRepository.findAll();
        return services.stream().map( service ->
            ServiceModel.builder()
                    .serviceId(service.getServiceId())
                    .Description(service.getDescription())
                    .name(service.getName())
                    .users(service.getUsers())
                    .build()
        ).collect(Collectors.toList());
    }

    @Override
    public ServiceModel getServiceById(Long id) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        "Service not Found by given id : "+ id,
                        "SERVICE_NOT_FOUND"));
        ServiceModel serviceModel = new ServiceModel();
        copyProperties(service, serviceModel);
        return serviceModel;
    }

    @Override
    public ServiceModel updateService(Long id, ServiceModel serviceModel) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        "Service Not Found by given id: "+ id,
                        "SERVICE_NOT_FOUND"
                ));
        service.setDescription(serviceModel.getDescription());
        service.setName(serviceModel.getName());
        serviceRepository.save(service);
        copyProperties(service, serviceModel);
        return serviceModel;
    }
}
