package com.example.springsecurityauthtwo.security.jwt;

import com.example.springsecurityauthtwo.security.services.UserDetailsServiceImpl;
import com.example.springsecurityauthtwo.security.tools.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader(Constants.HEADER_TOKEN);

        if (StringUtils.startsWith(requestToken, Constants.TOKEN_START)) {
            String jwt = requestToken.substring(7);
            try {
                String username = jwtUtils.getUsernameFromToken(jwt);
                if (StringUtils.isNotEmpty(username)
                        &&
                        null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails user = service.loadUserByUsername(username);
                    if (jwtUtils.validateToken(jwt, user)) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, user.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }

                }
            } catch (IllegalArgumentException e) {
                log.error("Unable to fetch JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token is expired");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.warn("JWT token does not begin with Bearer String");
        }
        filterChain.doFilter(request, response);

//        try {
//            String jwt = parseJwt(request);
//            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//                String username = jwtUtils.getUsernameFromJwtToken(jwt);
//                UserDetails userDetails = service.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, userDetails.getAuthorities());
//                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        } catch (Exception e) {
//            log.error("can't set user auth: %s", e);
//        }
//        filterChain.doFilter(request, response);
    }

//    private String parseJwt(HttpServletRequest request) {
//        return jwtUtils.getJwtFromCookie(request);
//    }

}
