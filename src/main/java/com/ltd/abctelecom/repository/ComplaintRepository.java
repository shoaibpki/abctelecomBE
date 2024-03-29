package com.ltd.abctelecom.repository;

import com.ltd.abctelecom.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByEngineerIdAndStatus(Long engineerId, String status);
    List<Complaint> findByEngineerId(Long engineerId);

    List<Complaint> findByStatus(String status);
}
