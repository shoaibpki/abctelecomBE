package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Services;
import com.ltd.abctelecom.model.ServiceModel;

import java.util.List;

public interface ServicesService {
    ServiceModel CreateService(ServiceModel service);

    List<ServiceModel> getAllService();

    ServiceModel getServiceById(Long id);

    ServiceModel updateService(Long id, ServiceModel serviceModel);
}
