package com.sanjeet.chat.service;


import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import com.sanjeet.chat.repository.ClientRepository;
import com.sanjeet.chat.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceDetails implements UserDetailsService {


    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceDetails(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch client details by email
        ClientRegistrationEntity client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new UsernameNotFoundException("Client not found with email: " + email);
        }
        // Return UserDetails object for Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(client.getEmail())
                .password(client.getPassword()) // Encrypted password
                .roles(Constant.CLIENT) // Assign role CLIENT
                .build();
    }

}
