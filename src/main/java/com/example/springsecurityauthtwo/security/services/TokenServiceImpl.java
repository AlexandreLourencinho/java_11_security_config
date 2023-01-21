package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.entities.TokenUsed;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;
import com.example.springsecurityauthtwo.security.repositories.TokenUsedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final UserServices userServices;
    private final AppUserRepository userRepository;
    private final TokenUsedRepository tokenRepository;

    /**
     *
     * @param username
     * @return
     */
    @Override
    public TokenUsed loadUsersTokens(String username) {
        log.info("loading user's tokens...");
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<TokenUsed> tokens = tokenRepository.findByUser(user.get());
            return tokens.orElse(null);
        } else {
            log.warn("couldn't find the user");
            return null;
        }
    }

    /**
     *
     * @param username
     */
    @Override
    public Boolean incrementRefreshAccount(String username) {
        log.info("incrementing refresh token account...");
        AppUser user = userServices.findUserByUsername(username);
        if (user != null && Boolean.TRUE.equals(isUserHadJwt(username))) {
            Optional<TokenUsed> token = tokenRepository.findByUser(user);
            if (token.isPresent()) {
                token.get().setRefreshTokenAccount(token.get().getRefreshTokenAccount() + 1);
                tokenRepository.save(token.get());
                log.info("user's token have been refreshed");
                return Boolean.TRUE;
            } else {
                log.warn("couldn't retrieve the tokens of the user.");
                return Boolean.FALSE;
            }
        } else {
            log.warn("couldn't retrieve the user with it's username.");
            return Boolean.FALSE;
        }
    }

    /**
     *
     * @param username
     * @return
     */
    @Override
    public Boolean isUserHadJwt(String username) {
        log.info("checking if the user had jwts before");
        AppUser user = userServices.findUserByUsername(username);
        if (user != null) {
            Optional<TokenUsed> token = tokenRepository.findByUser(user);
            if (token.isEmpty()) {
                log.info("user don't have any tokens in database");
                return Boolean.FALSE;
            } else {
                log.info("user already have his tokens in database");
                return Boolean.TRUE;
            }
        } else {
            log.warn("user wasn't found");
            return Boolean.FALSE;
        }
    }

    /**
     *
     * @param accessToken
     * @param refreshToken
     * @param username
     */
    @Override
    public void razUserJwt(String accessToken, String refreshToken, String username) {
        log.info("reinitializing user's tokens...");
        AppUser user = userServices.findUserByUsername(username);
        if (user != null) {
            Optional<TokenUsed> token = tokenRepository.findByUser(user);
            if (token.isPresent()) {
                token.get().setRefreshTokenAccount(1).setAccessToken(accessToken).setRefreshToken(refreshToken);
                tokenRepository.save(token.get());
                log.info("user's tokens had been reinitialized.");
            } else {
                log.warn("couldn't find a token to be reinitialized.");
            }
        } else {
            log.warn("couldn't retrieve the user to reinitialize his tokens.");
        }
    }

    /**
     *
     * @param accessToken
     * @param refreshToken
     * @param username
     * @return
     */
    @Override
    public TokenUsed registerJwtUser(String accessToken, String refreshToken, String username) {
        log.info("registering a new user's tokens...");
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            TokenUsed tokens = new TokenUsed().setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setUser(user.get())
                    .setRefreshTokenAccount(1)
                    .setEmitDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            log.info("user's token had been registered");
            return tokenRepository.save(tokens);
        } else {
            log.warn("couldn't register user's token : user wasn't found.");
            return null;
        }
    }


}
