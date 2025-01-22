package com.sanjeet.chat.service;

import com.sanjeet.chat.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {

    private final ClientRepository clientRepository;

    @Autowired
    public ApiKeyService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Validate the API key
    public boolean validateApiKey(String apiKey) {
        return clientRepository.existsByApiKey(apiKey);
    }
}
