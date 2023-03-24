package com.example.springsecurityauthtwo.security.model.mappers;

import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.dtos.UserInfoResponse;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * User info mapper
 */
@Mapper
public interface AppUserMapper {

    AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "customRoleMapper")
    UserInfoResponse appUserToUserInfoResponse(AppUser user);

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
