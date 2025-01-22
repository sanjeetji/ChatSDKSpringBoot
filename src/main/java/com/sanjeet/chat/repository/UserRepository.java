package com.sanjeet.chat.repository;

import com.sanjeet.chat.model.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDetailsEntity,Long> {

    @Query("SELECT u FROM user_details u WHERE u.phoneNumber = :phone_number AND u.apiKey = :api_key")
    Optional<UserDetailsEntity> findByPhoneNumber(@Param("phone_number") String phoneNumber, @Param("api_key") String apiKey);


}
