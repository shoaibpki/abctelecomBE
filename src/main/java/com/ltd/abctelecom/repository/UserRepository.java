package com.ltd.abctelecom.repository;

import com.ltd.abctelecom.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByIdAndRole(Long id, String role);
    Users findByPinCodeAndRole(String pinCode, String role);
    Users findByPinCode(String pinCode);
    Users findByPinCodeIgnoreCase(String pinCode);

    Users findByEmailAndPassword(String email, String password);
}
