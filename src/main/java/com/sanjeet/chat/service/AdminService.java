package com.sanjeet.chat.service;

import com.sanjeet.chat.model.dto.ClientRegistrationResponse;
import com.sanjeet.chat.model.entity.AdminRegistrationEntity;
import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import com.sanjeet.chat.repository.AdminRepository;
import com.sanjeet.chat.repository.ClientRepository;
import com.sanjeet.chat.utils.HandleClientInputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AdminService {


    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ClientRepository clientRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository,ClientRepository clientRepository){
        this.adminRepository = adminRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        this.clientRepository = clientRepository;
    }

    public ClientRegistrationResponse updateClientStatus(long id, boolean isActive) {
        ClientRegistrationEntity client = clientRepository.findById(id);
        if (client == null){
            throw new IllegalArgumentException("Client not found");
        }
        client.setActive(isActive);
        clientRepository.save(client);
        return new ClientRegistrationResponse(
                client.getClientId(),
                client.getCompanyName(),
                client.getPhoneNo(),
                client.getEmail(),
                client.getApiKey());

    }

    public void updateAdminSessionToken(AdminRegistrationEntity admin, String sessionToken) {
        if (admin == null) {
            throw new IllegalArgumentException("Client not found");
        }
        admin.setSessionToken(sessionToken);
        adminRepository.save(admin);
    }

    public List<ClientRegistrationEntity> fetchClients() {
        return clientRepository.findAll();
    }

    public ClientRegistrationEntity fetchClient(long clientId) {
        return clientRepository.findById(clientId);
    }

    public ClientRegistrationEntity getClientDetails(String apiKey) {
        return clientRepository.findByApiKey(apiKey);
    }

    public AdminRegistrationEntity registerAdmin(AdminRegistrationEntity request) {
        new HandleClientInputValidation().handleAdminRegistrationInput(request);
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        // Step 2: Save client details
        if (request.getCreatedAt() == null) {
            request.setCreatedAt(new Date()); // Current date and time
        }
        return adminRepository.save(request);
    }

    public Optional<AdminRegistrationEntity> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
