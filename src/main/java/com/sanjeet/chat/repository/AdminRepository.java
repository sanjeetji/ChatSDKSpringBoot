package com.sanjeet.chat.repository;

import com.sanjeet.chat.model.entity.AdminRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminRegistrationEntity,Long> {

    Optional<AdminRegistrationEntity> findByEmail(String email);
}
