package com.example.springsecurityauthtwo.security.jwt;

import com.example.springsecurityauthtwo.security.model.entities.TokenUsed;
import com.example.springsecurityauthtwo.security.services.TokenService;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;
    private TokenService tokenService;

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader(SecurityConstants.HEADER_TOKEN);
        if (StringUtils.startsWith(requestToken, SecurityConstants.TOKEN_START) ) {
            String jwt = requestToken.substring(SecurityConstants.BEARER_SUBSTRING);
            try {
                String username = jwtUtils.getUsernameFromToken(jwt);
                if (StringUtils.isNotEmpty(username)
                        &&
                        null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails user = userDetailsService.loadUserByUsername(username);
                    if (Boolean.TRUE.equals(jwtUtils.validateToken(jwt, user))) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (IllegalArgumentException e) {
                log.error("Unable to fetch JWT Token");
            } catch (ExpiredJwtException e) {
                manageExpiredToken(request, e);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.warn("JWT token does not begin with Bearer String " + requestToken);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * @param request
     * @param e
     */
    public void manageExpiredToken(HttpServletRequest request, ExpiredJwtException e) {
        log.error("JWT Token is expired");
        request.setAttribute(SecurityConstants.EXPIRED, e.getMessage());
        log.error("claims : " + e);
        log.error("claims : " + e.getClaims());
        // get the refresh jwt
        String refreshJwt = request.getHeader(SecurityConstants.REFRESH_TOKEN).substring(SecurityConstants.REFRESH_SUBSTRING);
        log.error(" test jwt 111" + Jwts.parser().setSigningKey("secret").parseClaimsJws(refreshJwt).getBody());

        // get username and comparing refresh jwt with actual datetime
        LocalDateTime date = LocalDateTime.now();
        log.error("before username");
        String username = e.getClaims().getSubject();
        log.error("after username : " + username);
        log.error("refresh exp date : " + e.getClaims().getExpiration());
        Boolean isRefreshExpired = e.getClaims().getExpiration().before(Date.from(date.atZone(ZoneId.systemDefault()).toInstant() ) );
        log.error("boolean expired : " + isRefreshExpired );
        UserDetails user = userDetailsService.loadUserByUsername(username);
        TokenUsed tokens = tokenService.loadUsersTokens(username);

        if (StringUtils.isNotEmpty(refreshJwt) && Boolean.FALSE.equals(isRefreshExpired) ) {
            log.error("here");
            if (user != null ) {
                log.info("refreshing the tokens....");
                String newToken = jwtUtils.generateJwtToken(user.getUsername() );
                request.setAttribute("newToken", newToken);
            } else {
                log.error("refresh token is invalid");
                request.setAttribute("invalidToken", "Refresh token is invalid");
            }

        } else if (Boolean.TRUE.equals(isRefreshExpired) && tokens.getRefreshTokenAccount() + 1 < SecurityConstants.MAX_REFRESH) {
            String newRefreshToken = jwtUtils.generateJwtRefreshToken(username);
            tokenService.incrementRefreshAccount(username);
            request.setAttribute("newRefreshToken", newRefreshToken);
            String newToken = jwtUtils.generateJwtToken(user.getUsername() );
            request.setAttribute("newToken", newToken);
        } else if (tokens.getRefreshTokenAccount() + 1 >= SecurityConstants.MAX_REFRESH) {
            log.error(SecurityConstants.REFRESHED_TOO_MUCH);
            request.setAttribute("refreshExpired", "Refresh token expired.");
        }
    }

}
