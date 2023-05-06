package com.example.springsecurityauthtwo.security.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Simple POJO used for double check confirmation deletion request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DeleteRequestConfirmation {

    private boolean deleteRequest;
    private boolean confirmedDeleteRequest;

}