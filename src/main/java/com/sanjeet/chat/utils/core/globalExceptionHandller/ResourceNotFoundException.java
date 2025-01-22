package com.sanjeet.chat.utils.core.globalExceptionHandller;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
