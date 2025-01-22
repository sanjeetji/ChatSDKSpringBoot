package com.sanjeet.chat.service;

import com.sanjeet.chat.model.entity.UserDetailsEntity;
import com.sanjeet.chat.repository.ClientRepository;
import com.sanjeet.chat.repository.UserRepository;
import com.sanjeet.chat.utils.HandleClientInputValidation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public UserService(UserRepository userRepository,ClientRepository clientRepository){
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }


    public UserDetailsEntity registerUser(UserDetailsEntity request) {
        new HandleClientInputValidation().handleUserRegistrationInput(request);
        return userRepository.save(request);
    }

    public void updateUserSessionToken(UserDetailsEntity user, String userAccessToken) {
        if (user == null) {
            throw new IllegalArgumentException("Client not found");
        }
        user.setUserSessionToken(userAccessToken);
        userRepository.save(user);
    }

    public Optional<UserDetailsEntity> findByPhoneNumber(String receiverPhone, String apiKey) {
        return userRepository.findByPhoneNumber(receiverPhone,apiKey);
    }
}
