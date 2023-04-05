package com.example.springsecurityauthtwo.security.services.users.implementations;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.services.users.interfaces.UserDetailsCustom;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class UserDetailsCustomImpl implements UserDetailsCustom {

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private List<GrantedAuthority> authorities;
    private String email;

    public UserDetailsCustomImpl(Long id, String username, String password, List<GrantedAuthority> authorities, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.email = email;
    }

    public static UserDetailsCustomImpl build(AppUser user) {
        List<GrantedAuthority> gAuthorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new UserDetailsCustomImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                gAuthorities,
                user.getEmail()
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
