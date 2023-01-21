package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.model.entities.TokenUsed;

public interface TokenService {
    TokenUsed loadUsersTokens(String username);
    Boolean incrementRefreshAccount(String username);
    Boolean isUserHadJwt(String username);

    void razUserJwt(String accessToken, String refreshToken, String username);

    TokenUsed registerJwtUser(String accessToken, String refreshToken, String username);
}
