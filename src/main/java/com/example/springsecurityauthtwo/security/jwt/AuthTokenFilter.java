package com.example.springsecurityauthtwo.security.jwt;

import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader(SecurityConstants.HEADER_TOKEN);
        if (StringUtils.startsWith(requestToken, SecurityConstants.TOKEN_START)) {
            String jwt = requestToken.substring(7);
            try {
                String username = jwtUtils.getUsernameFromToken(jwt);
                if (StringUtils.isNotEmpty(username)
                        &&
                        null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails user = service.loadUserByUsername(username);
                    if (Boolean.TRUE.equals(jwtUtils.validateToken(jwt, user))) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }

                }
            } catch (IllegalArgumentException e) {
                log.error("Unable to fetch JWT Token");
            } catch (ExpiredJwtException e) {
                request.setAttribute("expired", e.getMessage());
                log.error("JWT Token is expired");
                String fullRefreshJwt = request.getHeader(SecurityConstants.REFRESH_TOKEN);
                String refreshJwt = fullRefreshJwt.substring(8);
                Date date = new Date();
                if (fullRefreshJwt != null && jwtUtils.getExpirationDateFromToken(refreshJwt).after(date)) {
                    String username = jwtUtils.getUsernameFromToken(refreshJwt);
                    UserDetails user = service.loadUserByUsername(username);
                    if(user != null) {
                        Boolean validateRefreshToken = jwtUtils.validateToken(refreshJwt, user);
                        if(Boolean.TRUE.equals(validateRefreshToken)) {
                            String newToken = jwtUtils.generateJwtToken(user.getUsername());
                            request.setAttribute("newToken", newToken);
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.warn("JWT token does not begin with Bearer String");
        }
        filterChain.doFilter(request, response);
    }

}
