package com.example.springsecurityauthtwo.security.jwt.implementations;

import com.example.springsecurityauthtwo.security.jwt.interfaces.JwtUtils;
import com.example.springsecurityauthtwo.security.exceptions.TokenException;
import com.example.springsecurityauthtwo.security.jwt.interfaces.AuthTokenFilter;

import static com.example.springsecurityauthtwo.security.tools.constants.ErrorConstants.*;
import static com.example.springsecurityauthtwo.security.tools.constants.TokenConstants.*;
import static com.example.springsecurityauthtwo.security.tools.utils.ConstantsUtils.isDevOrTestEnv;
import static com.example.springsecurityauthtwo.security.tools.utils.ConstantsUtils.utilsGetAuthorizedUrl;
import static com.example.springsecurityauthtwo.security.tools.constants.DevAndLogConstants.ERROR_MANAGEMENT;

import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsCustom;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsServicesCustom;

import java.util.Arrays;
import java.io.IOException;
import java.util.Objects;

import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;



/**
 * @author Alexandre Lourencinho
 * @version 1.2
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilterImpl extends OncePerRequestFilter implements AuthTokenFilter {

    private JwtUtils jwtUtils;
    private UserDetailsServicesCustom userDetailsService;
    @Value("${spring.profiles.active}")
    private String profile;

    public AuthTokenFilterImpl(JwtUtils jwtUtils, UserDetailsServicesCustom userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * internal filter for request management
     *
     * @param request     the HttpServletRequest object
     * @param response    the HttpServletResponse object
     * @param filterChain FilterChain object provided by the servlet container giving a view into the invocation chain of a filtered request for a resource.
     * @throws ServletException Exception that can be thrown by the Servlet
     * @throws IOException      base exceptions which occur while reading or accessing files, directories and streams
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader(HEADER_TOKEN);
        final boolean isDevOrTestEnv = isDevOrTestEnv(profile);


        if (StringUtils.startsWith(requestToken, TOKEN_START)) {
            String jwt = requestToken.substring(BEARER_SUBSTRING);

            manageJwtAuthAndErrors(request, jwt);

        } else if (Boolean.TRUE.equals(this.manageAccessURL(request.getServletPath()))
                || Boolean.TRUE.equals(request.getServletPath().isBlank() && isDevOrTestEnv)
                || Boolean.TRUE.equals(Objects.isNull(requestToken) && isDevOrTestEnv)) {
            // TODO verifier condition ici si pas bypass toute sécurité
            filterChain.doFilter(request, response);
            return;
        } else {
            log.warn("JWT token does not begin with Bearer String " + requestToken);
            request.setAttribute(NO_BEARER, NO_BEARER_MESSAGE);
            throw new TokenException(NO_BEARER_MESSAGE);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void manageJwtAuthentication(HttpServletRequest request, String jwt) {

        String username = jwtUtils.getUsernameFromToken(jwt);

        if (StringUtils.isNotEmpty(username) &&
                null == SecurityContextHolder.getContext().getAuthentication()) {
            UserDetailsCustom user = userDetailsService.loadUserByUsername(username);

            if (Boolean.TRUE.equals(jwtUtils.validateToken(jwt, user))) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
    }

    @Override
    public void manageJwtAuthAndErrors(HttpServletRequest request, String jwt) {

        try {
            manageJwtAuthentication(request, jwt);

        } catch (ExpiredJwtException e) {
            log.error(ERROR_MANAGEMENT, EXPIRED_ERROR_MESSAGE, e.getMessage());
            request.setAttribute(EXPIRED, EXPIRED_ERROR_MESSAGE + e.getMessage());
            throw new TokenException(EXPIRED_ERROR_MESSAGE + e.getMessage());


        } catch (CompressionException e) {
            log.error(ERROR_MANAGEMENT, INCORRECT_TOKEN_FORMAT_MESSAGE, e.getMessage());
            request.setAttribute(INCORRECT_TOKEN_FORMAT, INCORRECT_TOKEN_FORMAT_MESSAGE + e.getMessage());
            throw new TokenException(INCORRECT_TOKEN_FORMAT_MESSAGE + e.getMessage());


        } catch (ClaimJwtException e) {
            log.error(ERROR_MANAGEMENT, CLAIMS_INVALID_MESSAGE, e.getMessage());
            request.setAttribute(CLAIMS_INVALID, CLAIMS_INVALID_MESSAGE + e.getMessage());
            throw new TokenException(CLAIMS_INVALID_MESSAGE + e.getMessage());


        } catch (MalformedJwtException e) {
            log.error(ERROR_MANAGEMENT, MALFORMED_JWT_MESSAGE, e.getMessage());
            request.setAttribute(MALFORMED, MALFORMED_JWT_MESSAGE + e.getMessage());
            throw new TokenException(MALFORMED_JWT_MESSAGE + e.getMessage());


        } catch (SecurityException e) {
            log.error(ERROR_MANAGEMENT, SIGNATURE_MESSAGE, e.getMessage());
            request.setAttribute(SIGNATURE, SIGNATURE_MESSAGE + e.getMessage());
            throw new TokenException(SIGNATURE_MESSAGE + e.getMessage());

        } catch (UnsupportedJwtException e) {
            log.error(ERROR_MANAGEMENT, UNSUPPORTED_MESSAGE, e.getMessage());
            request.setAttribute(UNSUPPORTED, UNSUPPORTED_MESSAGE + e.getMessage());
            throw new TokenException(UNSUPPORTED_MESSAGE + e.getMessage());

        } catch (Exception e) {
            log.error("catch exception e ");
            request.setAttribute(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);
            throw new TokenException(INTERNAL_SERVER_ERROR_MESSAGE + e.getMessage());
        }
    }

    public Boolean manageAccessURL(String servletPath) {
        return Arrays.stream(utilsGetAuthorizedUrl(isDevOrTestEnv(profile))).anyMatch(servletPath::contains);
    }


}
