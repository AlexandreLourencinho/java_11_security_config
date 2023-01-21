package com.example.springsecurityauthtwo.security.jwt;

import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        final String expired = (String) request.getAttribute(SecurityConstants.EXPIRED);
        final String refreshable = (String) request.getAttribute("refreshExpired");
        final String newRefreshToken = (String) request.getAttribute("newRefreshToken");
        log.error("new refresh token : " + newRefreshToken);
        final String invalidRefreshToken = (String) request.getAttribute("invalidToken");
        final Map<String, Object> body = new HashMap<>();
        final ObjectMapper mapper = new ObjectMapper();
        if (!StringUtils.isEmpty(expired) && StringUtils.isEmpty(refreshable)) {
            String refreshToken = (String) request.getAttribute("newToken");
            if (StringUtils.isNotEmpty(refreshToken)) {
                body.put("newToken", SecurityConstants.TOKEN_START + refreshToken);
            }
            if(StringUtils.isNotEmpty(newRefreshToken)) {
                body.put("newRefreshToken", SecurityConstants.TOKEN_START_REFRESH + newRefreshToken);
            }
            body.put(SecurityConstants.STATUS, HttpServletResponse.SC_FORBIDDEN);
            body.put(SecurityConstants.ERROR, SecurityConstants.EXPIRED);
            body.put(SecurityConstants.MESSAGE, expired);
            body.put(SecurityConstants.PATH, request.getServletPath());
        } else if(!StringUtils.isEmpty(expired) && !StringUtils.isEmpty(refreshable)) {
            body.put("refreshExpired", "The refresh token has expired. Please reconnect");
            body.put(SecurityConstants.STATUS, HttpServletResponse.SC_FORBIDDEN);
            body.put(SecurityConstants.ERROR, SecurityConstants.EXPIRED);
            body.put(SecurityConstants.MESSAGE, expired);
        }
        else {
            if(StringUtils.isNotEmpty(newRefreshToken)) {
                body.put("newRefreshToken", SecurityConstants.TOKEN_START_REFRESH + newRefreshToken);
            }
            if(StringUtils.isNotEmpty(invalidRefreshToken)) {
                log.error("else");
                body.put("invalidToken", "refresh token is invalid");
            }
            body.put(SecurityConstants.STATUS, HttpServletResponse.SC_UNAUTHORIZED);
            body.put(SecurityConstants.ERROR, "Unauthorized");
            body.put(SecurityConstants.MESSAGE, authException.getMessage());
            body.put(SecurityConstants.PATH, request.getServletPath());
        }
        mapper.writeValue(response.getOutputStream(), body);

    }

}
