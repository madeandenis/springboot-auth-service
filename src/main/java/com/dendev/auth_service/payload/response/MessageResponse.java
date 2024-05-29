package com.dendev.auth_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    public enum MessageType{
        success,
        error
    }
    private String type;
    private String message;
}
