package com.sanjeet.chat.utils;

import com.sanjeet.chat.model.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HandleApiResponseUtil {

    public ResponseEntity<ApiResponse<String>> handleApiFailedResponse(String message, HttpStatus code){
        System.out.println(message);
        ApiResponse<String> apiResponse = ApiResponse.error(ApiResponseFlag.FAILED.getFlag(),message);
        return ResponseEntity.status(code).body(apiResponse);
    }
}
