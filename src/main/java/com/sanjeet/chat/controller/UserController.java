package com.sanjeet.chat.controller;

import com.sanjeet.chat.model.dto.ApiResponse;
import com.sanjeet.chat.model.dto.UserDetailsResponse;
import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import com.sanjeet.chat.model.entity.UserDetailsEntity;
import com.sanjeet.chat.service.ApiKeyService;
import com.sanjeet.chat.service.ClientService;
import com.sanjeet.chat.service.JwtService;
import com.sanjeet.chat.service.UserService;
import com.sanjeet.chat.utils.ApiResponseFlag;
import com.sanjeet.chat.utils.HandleApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ApiKeyService apiKeyService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public UserController(ApiKeyService apiKeyService
            , JwtService jwtService
            , UserService userService
            , ClientService clientService
    ) {
        this.apiKeyService = apiKeyService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDetailsEntity request) {
        try {
            // Validate API key
            boolean isValidApiKey = apiKeyService.validateApiKey(request.getApiKey());
            if (!isValidApiKey) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
            }
            // Fetch client details
            ClientRegistrationEntity clientDetails = clientService.getClientDetails(request.getApiKey());
            if (clientDetails == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client details not found for the provided API Key");
            }
            Optional<UserDetailsEntity> userDetails = userService.findByPhoneNumber(request.getPhoneNumber(),request.getApiKey());
            if (userDetails.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ApiResponseFlag.FAILED.getFlag(), "User is already registered with this phone : "+ request.getPhoneNumber()));
            }
            // Associate user with the client
            request.setClient(clientDetails);
            if (request.getCreatedAt() == null) {
                request.setCreatedAt(new Date()); // Current date and time
            }
            UserDetailsEntity user  = userService.registerUser(request);
            // Generate session token (JWT)
            String userAccessToken = jwtService.generateUserToken(
                    String.valueOf(clientDetails.getClientId()),
                    String.valueOf(user.getUserId()),
                    String.valueOf(clientDetails.getApiKey()),request.getPhoneNumber());
            userService.updateUserSessionToken(user, userAccessToken);
            user.setUserSessionToken(userAccessToken);
            var userDetailsResponse = new UserDetailsResponse(
                    user.getUserId(),
                    user.getUsername(),
                    user.getUserImage(),
                    user.getPhoneNumber(),
                    user.getApiKey(),
                    user.getUserSessionToken(),
                    user.getCreatedAt());
            ApiResponse<UserDetailsResponse> apiResponse = ApiResponse.success(ApiResponseFlag.SUCCESS.getFlag(), "Success",userDetailsResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }catch (Exception e){
            return new HandleApiResponseUtil().handleApiFailedResponse("Something went wrong "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
