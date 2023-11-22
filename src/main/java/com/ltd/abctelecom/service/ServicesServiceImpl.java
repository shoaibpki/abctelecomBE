package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Services;
import com.ltd.abctelecom.exception.CustomException;
import com.ltd.abctelecom.model.ServiceModel;
import com.ltd.abctelecom.repository.ServiceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class ServicesServiceImpl implements ServicesService{

    @Autowired
    ServiceRepository serviceRepository;
    @Override
    public ServiceModel CreateService(ServiceModel service) {
        Services services = Services.builder()
                .name(service.getName())
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
                    .name(service.getName())
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
}
