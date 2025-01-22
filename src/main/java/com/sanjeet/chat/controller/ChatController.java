package com.sanjeet.chat.controller;


import com.sanjeet.chat.model.dto.ApiResponse;
import com.sanjeet.chat.model.dto.MessageDetailsResponse;
import com.sanjeet.chat.model.entity.MessageDetailsEntity;
import com.sanjeet.chat.model.entity.UserDetailsEntity;
import com.sanjeet.chat.service.ChatService;
import com.sanjeet.chat.service.UserService;
import com.sanjeet.chat.utils.ApiResponseFlag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {


    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatService chatService,UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> postChat(@RequestBody MessageDetailsEntity data, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                String apiKey = request.getHeader("API_KEY");
                Optional<UserDetailsEntity> receiver = userService.findByPhoneNumber(data.getReceiverPhone(), apiKey);
                System.out.println("Request Data = " + data);
                if (receiver.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ApiResponseFlag.NOT_FOUND.getFlag(), "Receiver is not found with this phone no : "+data.getReceiverPhone()));
                }
                if (data.getCreatedAt() == null) {
                    data.setCreatedAt(new Date()); // Current date and time
                }
                MessageDetailsEntity response = chatService.postChat(data,token,apiKey);
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(ApiResponseFlag.SUCCESS.getFlag(), "Message Post Successful.",response));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(ApiResponseFlag.FAILED.getFlag(),"Authorization header is missing or invalid"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> fetchChat(@RequestParam("receiverPhone") String receiverPhone, HttpServletRequest request) {
        try {
                String authorizationHeader = request.getHeader("Authorization");
                String apiKey = request.getHeader("API_KEY");
                if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.error(ApiResponseFlag.FAILED.getFlag(), "Authorization header is missing or invalid"));
                }
                if (apiKey == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(ApiResponseFlag.FAILED.getFlag(), "API_KEY header is missing"));
                }
                // Normalize receiverPhone
                if (!receiverPhone.startsWith("+")) {
                    receiverPhone = "+" + receiverPhone.trim();
                }
                String token = authorizationHeader.substring(7);
                System.out.println("receiverPhone = " + receiverPhone + ", request = " + request + " Api key is : "+apiKey);
                Optional<UserDetailsEntity> receiver = userService.findByPhoneNumber(receiverPhone, apiKey);
                if (receiver.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ApiResponseFlag.NOT_FOUND.getFlag(), "Receiver is not found with this phone no : "+receiverPhone));
                }
                List<MessageDetailsResponse> response = chatService.fetchChat(token,receiverPhone,apiKey);
                ApiResponse<MessageDetailsResponse> apiResponse = ApiResponse.success(ApiResponseFlag.SUCCESS.getFlag(),"Message fetched successful.",response);
                return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = ApiResponse.error(ApiResponseFlag.FAILED.getFlag(),e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

}