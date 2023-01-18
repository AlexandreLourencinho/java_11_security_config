package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model of message response for request to the controller
 */
@Data
@AllArgsConstructor
public class MessageResponse {

    private String message;

}
