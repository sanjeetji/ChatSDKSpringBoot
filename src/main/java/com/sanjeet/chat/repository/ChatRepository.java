package com.sanjeet.chat.repository;

import com.sanjeet.chat.model.entity.MessageDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<MessageDetailsEntity,Long> {


    @Query("SELECT m FROM message_details m WHERE m.secretKey = :secret_key")
    List<MessageDetailsEntity> findAllBySecretKey(@Param("secret_key") String secretKey);

    @Query("SELECT m FROM message_details m WHERE m.receiverPhone = :receiver_phone")
    List<MessageDetailsEntity> findAllByPhoneNumber(@Param("receiver_phone") String receiverPhone);
}
