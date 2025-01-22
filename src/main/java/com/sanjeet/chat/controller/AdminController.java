package com.sanjeet.chat.controller;


import com.sanjeet.chat.model.dto.AdminLoginRequest;
import com.sanjeet.chat.model.dto.AdminRegistrationResponse;
import com.sanjeet.chat.model.dto.ApiResponse;
import com.sanjeet.chat.model.entity.AdminRegistrationEntity;
import com.sanjeet.chat.service.AdminService;
import com.sanjeet.chat.service.JwtService;
import com.sanjeet.chat.utils.ApiResponseFlag;
import com.sanjeet.chat.utils.HandleApiResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AdminController(AdminService adminService,JwtService jwtService,AuthenticationManager authenticationManager){
        this.adminService = adminService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@Valid @RequestBody AdminRegistrationEntity request) {
        // Generate a unique API key
        try {
            // Step 1: Check admin is already in db or not
            Optional<AdminRegistrationEntity> savedAdmin  = adminService.findByEmail(request.getEmail());
            if (savedAdmin.isPresent()){
                return new HandleApiResponseUtil().handleApiFailedResponse("Client already register with this email : "+ request.getEmail(),HttpStatus.CONFLICT);
            }



            // Step 2: Register the admin
            if (request.getCreatedAt() == null) {
                request.setCreatedAt(new Date()); // Current date and time
            }
            request.setSessionToken("");
            AdminRegistrationEntity adminResponse  = adminService.registerAdmin(request);
            if (adminResponse != null){
                AdminRegistrationResponse response = new AdminRegistrationResponse(adminResponse.getId(),
                        adminResponse.getEmail(),
                        adminResponse.getPassword(),
                        adminResponse.getPhoneNo(),
                        adminResponse.getCreatedAt());
                ApiResponse<AdminRegistrationResponse> apiResponse =  ApiResponse.success(ApiResponseFlag.SUCCESS.getFlag(),"Congratulation, You have register successful.",response);
                return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
            }else {
                return new HandleApiResponseUtil().handleApiFailedResponse("Something went wrong while registering: ",HttpStatus.BAD_REQUEST);
            }
        }catch (DataIntegrityViolationException e) {
            return new HandleApiResponseUtil().handleApiFailedResponse("Client already registered with this email: " + request.getEmail(), HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new HandleApiResponseUtil().handleApiFailedResponse("Something went wrong while registering: "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {
        try {
            System.out.println("Login request received: " + request);
            // Step 1: Authenticate email and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            if (authentication.isAuthenticated()) {
                // Save authentication to SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Step 2: Generate a session token for the client
                Optional<AdminRegistrationEntity> admin = adminService.findByEmail(request.getEmail());
                if (admin.isPresent()){
                    System.out.println("Admin found: " + admin.get());
                    String sessionToken = jwtService.generateAdminSessionToken(request.getEmail());
                    adminService.updateAdminSessionToken(admin.get(), sessionToken);
                    AdminRegistrationResponse adminResponse = new AdminRegistrationResponse(
                            admin.get().getId(),
                            admin.get().getEmail(),
                            admin.get().getPassword(),
                            admin.get().getPhoneNo(),
                            sessionToken,
                            admin.get().getCreatedAt()
                    );
                    return ResponseEntity.ok(new ApiResponse<>(ApiResponseFlag.SUCCESS.getFlag(), "Login successful", adminResponse));
                }else {
                    return new HandleApiResponseUtil().handleApiFailedResponse("Invalid CLIENT_API_KEY or email does not match the admin record.",HttpStatus.FORBIDDEN);
                }
            }else {
                return new HandleApiResponseUtil().handleApiFailedResponse("You are not authorized",HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HandleApiResponseUtil().handleApiFailedResponse("Login failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


}
