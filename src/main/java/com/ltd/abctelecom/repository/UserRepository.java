package com.ltd.abctelecom.repository;

import com.ltd.abctelecom.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmailAndPassword(String email, String password);
}
