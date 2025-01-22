package com.sanjeet.chat.utils.core.globalExceptionHandller;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
