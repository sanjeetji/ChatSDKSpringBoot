package com.sanjeet.chat.repository;

import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientRegistrationEntity,Long> {

    boolean existsByApiKey(String apiKey);

    @Query("SELECT c.secretKey FROM client_Details c WHERE c.apiKey = :api_key")
    String findSecretKeyByApiKey(@Param("api_key") String apiKey);

    @Query("SELECT COUNT(c) > 0 FROM client_Details c WHERE c.apiKey = :api_key AND c.isActive = true")
    boolean validateApiKeyOld(@Param("api_key") String apiKey);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM client_Details c WHERE c.apiKey = :api_key AND c.isActive = true")
    boolean validateApiKey(@Param("api_key") String apiKey);

    ClientRegistrationEntity findById(long id);

    @Query("SELECT c.secretKey FROM client_Details c WHERE c.apiKey = :api_key")
    String getSecretKey(@Param("api_key") String apiKey);

    ClientRegistrationEntity findByApiKey(String apiKey);

    ClientRegistrationEntity findByEmail(String email);
}
