package com.sanjeet.chat.service;


import com.sanjeet.chat.model.entity.AdminRegistrationEntity;
import com.sanjeet.chat.repository.AdminRepository;
import com.sanjeet.chat.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceDetails implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceDetails(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch admin details by email
        System.out.println("AdminServiceDetails: loadUserByUsername called with email = " + email);
        Optional<AdminRegistrationEntity> admin = adminRepository.findByEmail(email);
        if (admin.isEmpty()) {
            throw new UsernameNotFoundException("Admin not found with email: " + email);
        }
        // Return UserDetails object for Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(admin.get().getEmail())
                .password(admin.get().getPassword()) // Encrypted password
                .roles(Constant.ADMIN) // Assign role ADMIN
                .build();
    }
}
