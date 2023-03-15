package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Model of message response for request to the controller
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private String message;

}
