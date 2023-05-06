package com.example.springsecurityauthtwo.security.jwt;

import static com.example.springsecurityauthtwo.security.tools.constants.ErrorConstants.*;
import static com.example.springsecurityauthtwo.security.tools.constants.TokenConstants.*;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * The authentication entry point management for authentication scheme
 *
 * @author Alexandre Lourencinho
 * @version 1.1
 */
@Slf4j
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {


    /**
     * manage the entry points errors
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException if I/O exception error occurred
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        log.error("Unauthorized error: {}, {}", authException.getMessage(), authException.getClass());

        final Map<String, Object> body = new HashMap<>();
        final ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Arrays.stream(getErrorList()).forEach(error -> {
            if (Objects.nonNull(request.getAttribute(error))) {
                String errMessage = null;
                body.put(STATUS, HttpServletResponse.SC_FORBIDDEN);
                body.put(ERROR, error);
                for (String errorMessage : getErrorsMessageList()) {
                    if (errorMessage.startsWith(error)) {
                        errMessage = errorMessage;
                        break;
                    }
                }
                body.put(MESSAGE, errMessage);
            }
        });
        if(Objects.isNull(body.get(ERROR))) {
            body.put(ERROR, ERROR);
            body.put(MESSAGE, UNSUPPORTED_MESSAGE);
        }
        body.put(PATH, request.getServletPath());
        mapper.writeValue(response.getOutputStream(), body);

    }

}
