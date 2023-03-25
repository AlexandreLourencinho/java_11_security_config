package com.example.springsecurityauthtwo.security.model.mappers;

import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.services.UserDetailsCustom;
import com.example.springsecurityauthtwo.security.model.dtos.UserInfoResponse;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;

/**
 * Mapping interface for user classes : AppUser, UserInfoResponse...
 *
 * @author Lourencinho Alexandre
 * @version 1.0.0
 */
@Mapper
public interface AppUserMapper {

    AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "customRoleMapper")
    UserInfoResponse appUserToUserInfoResponse(AppUser user);

    default UserInfoResponse userDetailsToUserInfoResponse(UserDetailsCustom user) {
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new UserInfoResponse().setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setRoles(roles);
    }

    @Named("customRoleMapper")
    default List<String> customRoleMapper(Set<AppRole> roles) {
        List<String> listRoles = new ArrayList<>();
        roles.forEach(role -> {
            String roleName = role.getName().toString();
            listRoles.add(roleName);
        });
        return listRoles;
    }
}
