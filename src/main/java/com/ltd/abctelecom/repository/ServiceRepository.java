package com.ltd.abctelecom.repository;

import com.ltd.abctelecom.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

}
