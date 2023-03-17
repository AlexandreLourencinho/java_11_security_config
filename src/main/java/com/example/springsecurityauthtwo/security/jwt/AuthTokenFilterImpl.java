package com.example.springsecurityauthtwo.security.jwt;

import com.example.springsecurityauthtwo.security.exceptions.TokenException;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Alexandre Lourencinho
 * @version 1.1
 */
@Slf4j
@AllArgsConstructor
public class AuthTokenFilterImpl extends OncePerRequestFilter implements  AuthTokenFilter{

    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;

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

        if (StringUtils.startsWith(requestToken, SecurityConstants.TOKEN_START)) {
            String jwt = requestToken.substring(SecurityConstants.BEARER_SUBSTRING);

            manageJwtAuthAndErrors(request, jwt);

        } else if (!request.getServletPath().isBlank() && !request.getServletPath().contains(SecurityConstants.SIGNUP_URL) && !request.getServletPath().contains(SecurityConstants.SIGNING_URL)) {
            log.warn("JWT token does not begin with Bearer String " + requestToken);
            request.setAttribute(SecurityConstants.NOBEARER, SecurityConstants.NOBEARER_MESSAGE);
            throw new TokenException(SecurityConstants.NOBEARER_MESSAGE);
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
            UserDetails user = userDetailsService.loadUserByUsername(username);

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
