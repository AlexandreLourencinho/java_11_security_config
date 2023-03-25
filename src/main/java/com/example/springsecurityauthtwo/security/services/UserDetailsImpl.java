package com.example.springsecurityauthtwo.security.services;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * an implementation of the UserDetails interface
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetailsCustom {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Build method to build that class from a user
     *
     * @param user the AppUser object
     * @return instantiation of the implementation
     */
    public static UserDetailsImpl build(AppUser user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public String getEmail() {
        return this.email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
