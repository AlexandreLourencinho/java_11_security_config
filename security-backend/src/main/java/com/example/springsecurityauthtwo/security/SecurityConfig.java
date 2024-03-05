package com.example.springsecurityauthtwo.security;

import com.example.springsecurityauthtwo.security.jwt.AuthEntryPoint;
import com.example.springsecurityauthtwo.security.jwt.interfaces.JwtUtils;
import com.example.springsecurityauthtwo.security.jwt.implementations.AuthTokenFilterImpl;

import static com.example.springsecurityauthtwo.security.tools.utils.ConstantsUtils.isDevOrTestEnv;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsServicesCustom;
import static com.example.springsecurityauthtwo.security.tools.constants.DevAndLogConstants.DEV_FILTER_CHAIN_LOG;
import static com.example.springsecurityauthtwo.security.tools.utils.ConstantsUtils.utilsGetAuthorizedUrlPattern;
import static com.example.springsecurityauthtwo.security.tools.constants.DevAndLogConstants.PROD_FILTER_CHAIN_LOG;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
        Boolean isProfileDevOrTest = isDevOrTestEnv(profile);
        String[] matchingUrlPermitAll = utilsGetAuthorizedUrlPattern(isProfileDevOrTest);
        if (Boolean.TRUE.equals(isProfileDevOrTest)) {
            log.info("authorized URL : {}", Arrays.toString(matchingUrlPermitAll));
        }
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        if (Boolean.TRUE.equals(isProfileDevOrTest)) {
            log.info(DEV_FILTER_CHAIN_LOG);
            http.authorizeHttpRequests(request -> request.requestMatchers(matchingUrlPermitAll).permitAll());
            http.authorizeHttpRequests(request -> request.anyRequest().permitAll());
        } else {
            log.info(PROD_FILTER_CHAIN_LOG);
            http.authorizeHttpRequests(request -> request.requestMatchers("/actuator/**").hasRole("ACTUATOR"));
            http.authorizeHttpRequests(request -> request.requestMatchers(matchingUrlPermitAll).permitAll());
            http.authorizeHttpRequests(request -> request.anyRequest().authenticated());
        }
        http.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.authenticationProvider(authProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
