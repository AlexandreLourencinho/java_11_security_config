package com.example.springsecurityauthtwo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {


    /**
     * manage the entry points errors
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException if I/O exception error occured
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final String expired = (String) request.getAttribute("expired");
        final Map<String, Object> body = new HashMap<>();
        final ObjectMapper mapper = new ObjectMapper();
        if (expired != null) {
            String refreshToken = (String) request.getAttribute("newToken");
            if (refreshToken != null) {
                body.put("newToken", refreshToken);
                body.put("isRefreshTokenVlid", true);
                // TODO refresh token de 8 heures
            }
            body.put("status", HttpServletResponse.SC_FORBIDDEN);
            body.put("error", "expired");
            body.put("message", expired);
            body.put("path", request.getServletPath());
        } else {
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Unauthorized");
            body.put("message", authException.getMessage());
            body.put("path", request.getServletPath());
        }
        mapper.writeValue(response.getOutputStream(), body);

    }

}
