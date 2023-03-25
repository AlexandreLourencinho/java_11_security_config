package com.example.springsecurityauthtwo.security;

import com.example.springsecurityauthtwo.security.jwt.JwtUtils;
import com.example.springsecurityauthtwo.security.jwt.AuthEntryPoint;
import com.example.springsecurityauthtwo.security.jwt.AuthTokenFilterImpl;
import com.example.springsecurityauthtwo.security.services.UserDetailsServicesCustom;
import com.example.springsecurityauthtwo.security.tools.SecurityConstants;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;


/**
 * Security configuration class
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private final UserDetailsServicesCustom userDetailsService;
    private final AuthEntryPoint unauthorizedHandler;
    private final JwtUtils jwtUtils;
    @Value("${spring.profiles.active}")
    private String profile;


    @Autowired
    public SecurityConfig(UserDetailsServicesCustom userDetailsService, AuthEntryPoint unauthorizedHandler, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Bean providing the auth provider
     *
     * @return a DaoAuthenticationProvider object
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Bean returning the authentication manager
     *
     * @param authConfig AuthenticationConfiguration object
     * @return the authentication manager
     * @throws Exception if something went bad
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Bean returning the Entry point filter
     *
     * @return the filter
     */
    @Bean
    public AuthTokenFilterImpl authTokenFilter() {
        return new AuthTokenFilterImpl(jwtUtils, userDetailsService);
    }

    /**
     * Bean providing the password encoder
     *
     * @return a BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * The filter chain applied to the routes. Default permitted : signup, signing. h2 is present for dev purpose.
     * Defines the filters, the auth provider that will be used
     *
     * @param http the HttpSecurity object
     * @return a SecurityFilterChain
     * @throws Exception is something went wrong
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        Boolean profileDev = profile.equals(SecurityConstants.DEV_ENV);
        String[] matchingUrlPermitAll = SecurityConstants.getAuthorizedUrl(profileDev);
        if (Boolean.TRUE.equals(profileDev)) {
            log.info("authorized URL : {}", Arrays.toString(matchingUrlPermitAll));
        }

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/actuator/**").hasRole("ACTUATOR")
                .antMatchers(matchingUrlPermitAll).permitAll()
                .anyRequest().authenticated();
        http.headers().frameOptions().sameOrigin();
        http.authenticationProvider(authProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
