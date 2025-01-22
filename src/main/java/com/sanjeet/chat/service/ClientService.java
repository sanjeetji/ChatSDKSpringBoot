package com.sanjeet.chat.service;

import com.sanjeet.chat.model.dto.ClientRegistrationResponse;
import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import com.sanjeet.chat.repository.ClientRepository;
import com.sanjeet.chat.utils.HandleClientInputValidation;
import com.sanjeet.chat.utils.SecretKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.sanjeet.chat.utils.Constant.H_MAC_ALGORITHM;


@Service
public class ClientService {


    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    }

    public ClientRegistrationEntity registerClient(ClientRegistrationEntity request) {
        new HandleClientInputValidation().handleClientRegistrationInput(request);
        // Step 1: Generate secret key and API key
        String secretKey = SecretKeyGenerator.generateKey(); // Generate a unique secret key
        String apiKey = generateApiKey(request, secretKey);
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        // Step 2: Save client details
        if (request.getCreatedAt() == null) {
            request.setCreatedAt(new Date()); // Current date and time
        }
        ClientRegistrationEntity newClient = new ClientRegistrationEntity(
                request.getCompanyName(),
                request.getEmail(),
                request.getPhoneNo(),
                apiKey,
                secretKey,
                true, // Client is active by default
                "" ,   // Session token will be generated later,
                request.getPassword(),
                request.getCreatedAt()
        );
        return clientRepository.save(newClient);
    }

    // Generate a secure API key
    public String generateApiKey(ClientRegistrationEntity request, String secretKey) {
        String input = request.getCompanyName() + request.getEmail() + request.getPhoneNo();
        String uuid = UUID.randomUUID().toString();
        try {
            Mac hmac = Mac.getInstance(H_MAC_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), H_MAC_ALGORITHM);
            hmac.init(secretKeySpec);
            byte[] hash = hmac.doFinal((input + uuid).getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating API key", e);
        }
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

    public void updateClientSessionToken(ClientRegistrationEntity client, String sessionToken) {
        if (client == null) {
            throw new IllegalArgumentException("Client not found");
        }
        client.setSessionToken(sessionToken);
        clientRepository.save(client);
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

    public ClientRegistrationEntity getClientDetailsByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}
