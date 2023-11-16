package com.ltd.abctelecom.repository;

import com.ltd.abctelecom.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmailAndPassword(String email, String password);
}
