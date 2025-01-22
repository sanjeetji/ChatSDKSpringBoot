package com.sanjeet.chat.controller;

import com.sanjeet.chat.model.dto.ApiResponse;
import com.sanjeet.chat.model.dto.ClientAuthRequest;
import com.sanjeet.chat.model.dto.ClientRegistrationResponse;
import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import com.sanjeet.chat.service.ClientService;
import com.sanjeet.chat.service.JwtService;
import com.sanjeet.chat.utils.ApiResponseFlag;
import com.sanjeet.chat.utils.HandleApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final ClientService clientService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public ClientController(ClientService clientService, JwtService jwtService,AuthenticationManager authenticationManager) {
        this.clientService = clientService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody ClientRegistrationEntity request) {
        // Generate a unique API key
        try {
            // Step 1: Register the client
            if (request.getCreatedAt() == null) {
                request.setCreatedAt(new Date()); // Current date and time
            }
            request.setSessionToken("");
            ClientRegistrationEntity client  = clientService.registerClient(request);
            ClientRegistrationResponse clientResponse = new ClientRegistrationResponse(
                    client.getClientId(),
                    client.getCompanyName(),
                    client.getPhoneNo(),
                    client.getEmail(),
                    client.getApiKey(),
                    client.isActive(),
                    client.getPassword(),
                    client.getCreatedAt()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(ApiResponseFlag.SUCCESS.getFlag(),"Congratulation, You have register successful.",clientResponse));
        }catch (Exception e){
            return new HandleApiResponseUtil().handleApiFailedResponse("Something went wrong to register client..."+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> clientLogin(@RequestBody ClientAuthRequest request) {
        try {
            // Step 1: Authenticate email and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            if (authentication.isAuthenticated()) {
                // Save authentication to SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Step 2: Validate CLIENT USERNAME
                ClientRegistrationEntity client = clientService.getClientDetailsByEmail(request.getEmail());
                if (client == null || !client.getEmail().equals(request.getEmail())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid CLIENT_API_KEY or email does not match the client record.");
                }
                // Step 2: Generate a session token for the client
                String sessionToken = jwtService.generateClientToken(String.valueOf(client.getClientId()), client.getApiKey(),client.getEmail());
                clientService.updateClientSessionToken(client, sessionToken);
                // Step 5: Build response
                ClientRegistrationResponse clientResponse = new ClientRegistrationResponse(
                        client.getClientId(),
                        client.getCompanyName(),
                        client.getPhoneNo(),
                        client.getEmail(),
                        client.getApiKey(),
                        client.isActive(),
                        sessionToken
                );
                return ResponseEntity.ok(new ApiResponse<>(ApiResponseFlag.SUCCESS.getFlag(), "Login successful", clientResponse));
            }else {
                return new HandleApiResponseUtil().handleApiFailedResponse("You are not authorized...",HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new HandleApiResponseUtil().handleApiFailedResponse("Login failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/client_details")
    public ResponseEntity<?> getClientDetails(@RequestHeader("Authorization") String sessionToken) {
        try {
            String authorizationHeader = sessionToken.substring(7);
            System.out.println("Authorization Header = " + authorizationHeader + " sessionToken " + sessionToken);
            // Extract the API Key from the session token
            String apiKey = jwtService.extractApiKeyFromToken(authorizationHeader);
            // Fetch client details based on API Key
            ClientRegistrationEntity clientDetails = clientService.getClientDetails(apiKey);
            if (clientDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client details not found");
            }
            ApiResponse<ClientRegistrationEntity> apiResponse = ApiResponse.success(ApiResponseFlag.SUCCESS.getFlag(),"Client details fetched successfully.",clientDetails);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            return new HandleApiResponseUtil().handleApiFailedResponse("Something went wrong to fetch client details : "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
