package com.sanjeet.chat.utils.core.globalExceptionHandller;

public class CustomBusinessException extends RuntimeException {
    private final String errorCode;

    public CustomBusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
