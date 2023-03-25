package com.example.springsecurityauthtwo.security.jwt.implementations;

import com.example.springsecurityauthtwo.security.jwt.interfaces.AuthTokenFilter;
import com.example.springsecurityauthtwo.security.jwt.interfaces.JwtUtils;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsCustom;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsServicesCustom;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import com.example.springsecurityauthtwo.security.exceptions.TokenException;

import java.util.Arrays;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;


/**
 * @author Alexandre Lourencinho
 * @version 1.1
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilterImpl extends OncePerRequestFilter implements AuthTokenFilter {

    private JwtUtils jwtUtils;
    private UserDetailsServicesCustom userDetailsService;
    @Autowired
    private Environment environment;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader(SecurityConstants.HEADER_TOKEN);
        log.info("active profile : {}", Arrays.toString(environment.getActiveProfiles()));


        if (StringUtils.startsWith(requestToken, SecurityConstants.TOKEN_START)) {
            String jwt = requestToken.substring(SecurityConstants.BEARER_SUBSTRING);

            manageJwtAuthAndErrors(request, jwt);

        } else if (!request.getServletPath().isBlank() && !request.getServletPath().contains(SecurityConstants.PUBLIC_URL) && !request.getServletPath().contains("swagger-ui")
                && !request.getServletPath().contains("/v3/api-docs") && !request.getServletPath().contains(SecurityConstants.H2_CONSOLE_URL)) {
            // TODO trouver moyen de gérer ça mieux que ça î
            log.warn("JWT token does not begin with Bearer String " + requestToken);
            request.setAttribute(SecurityConstants.NO_BEARER, SecurityConstants.NO_BEARER_MESSAGE);
            throw new TokenException(SecurityConstants.NO_BEARER_MESSAGE);
        } else {
            request.setAttribute(SecurityConstants.UNAUTHORIZED, SecurityConstants.UNAUTHORIZED_MESSAGE);
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
            log.error(SecurityConstants.ERROR_MANAGEMENT, SecurityConstants.EXPIRED_ERROR_MESSAGE, e.getMessage());
            request.setAttribute(SecurityConstants.EXPIRED, SecurityConstants.EXPIRED_ERROR_MESSAGE + e.getMessage());
            throw new TokenException(SecurityConstants.EXPIRED_ERROR_MESSAGE + e.getMessage());


        } catch (CompressionException e) {
            log.error(SecurityConstants.ERROR_MANAGEMENT, SecurityConstants.INCORRECT_TOKEN_FORMAT_MESSAGE, e.getMessage());
            request.setAttribute(SecurityConstants.INCORRECT_TOKEN_FORMAT, SecurityConstants.INCORRECT_TOKEN_FORMAT_MESSAGE + e.getMessage());
            throw new TokenException(SecurityConstants.INCORRECT_TOKEN_FORMAT_MESSAGE + e.getMessage());


        } catch (ClaimJwtException e) {
            log.error(SecurityConstants.ERROR_MANAGEMENT, SecurityConstants.CLAIMS_INVALID_MESSAGE, e.getMessage());
            request.setAttribute(SecurityConstants.CLAIMS_INVALID, SecurityConstants.CLAIMS_INVALID_MESSAGE + e.getMessage());
            throw new TokenException(SecurityConstants.CLAIMS_INVALID_MESSAGE + e.getMessage());


        } catch (MalformedJwtException e) {
            log.error(SecurityConstants.ERROR_MANAGEMENT, SecurityConstants.MALFORMED_JWT_MESSAGE, e.getMessage());
            request.setAttribute(SecurityConstants.MALFORMED, SecurityConstants.MALFORMED_JWT_MESSAGE + e.getMessage());
            throw new TokenException(SecurityConstants.MALFORMED_JWT_MESSAGE + e.getMessage());


        } catch (SignatureException e) {
            log.error(SecurityConstants.ERROR_MANAGEMENT, SecurityConstants.SIGNATURE_MESSAGE, e.getMessage());
            request.setAttribute(SecurityConstants.SIGNATURE, SecurityConstants.SIGNATURE_MESSAGE + e.getMessage());
            throw new TokenException(SecurityConstants.SIGNATURE_MESSAGE + e.getMessage());

        } catch (UnsupportedJwtException e) {
            log.error(SecurityConstants.ERROR_MANAGEMENT, SecurityConstants.UNSUPPORTED_MESSAGE, e.getMessage());
            request.setAttribute(SecurityConstants.UNSUPPORTED, SecurityConstants.UNSUPPORTED_MESSAGE + e.getMessage());
            throw new TokenException(SecurityConstants.UNSUPPORTED_MESSAGE + e.getMessage());

        } catch (Exception e) {
            log.error("catch exception e ");
        }
    }

}
