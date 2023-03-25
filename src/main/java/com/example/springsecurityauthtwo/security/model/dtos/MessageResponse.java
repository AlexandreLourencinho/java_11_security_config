package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


/**
 * Model of message response for request to the controller
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private String message;

}
