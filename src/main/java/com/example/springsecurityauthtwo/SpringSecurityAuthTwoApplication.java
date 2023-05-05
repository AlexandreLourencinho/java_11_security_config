package com.example.springsecurityauthtwo;

import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import com.example.springsecurityauthtwo.security.repositories.AppRoleRepository;
import com.example.springsecurityauthtwo.security.repositories.AppUserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
public class SpringSecurityAuthTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityAuthTwoApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AppRoleRepository roleRepository, AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return (args -> {
            log.warn("bean CommandLineRunner launched");
            Iterable<AppRole> test = roleRepository.findAll();
            if(!test.iterator().hasNext()) {
                String[] eRolesAr = {ERole.ROLE_ADMIN.getValue(), ERole.ROLE_USER.getValue(), ERole.ROLE_MODERATOR.getValue(), ERole.ROLE_ACTUATOR.getValue()};
                List<String> listRoles = new ArrayList<>(List.of(eRolesAr));
                listRoles.forEach(role -> {
                    AppRole rol = new AppRole().setName(role);
                    roleRepository.save(rol);
                });
            }
            Iterable<AppUser> testUser = userRepository.findAll();
            if(!testUser.iterator().hasNext()) {
                Set<AppRole> setRoles = new HashSet<>();
                AppRole role = roleRepository.findByName(ERole.ROLE_ADMIN.getValue()).orElse(null);
                AppRole secondRole = roleRepository.findByName(ERole.ROLE_USER.getValue()).orElse(null);
                setRoles.add(role);
                setRoles.add(secondRole);
                AppUser admin = new AppUser().setUsername("admin")
                        .setEmail("admin@admin")
                        .setPassword(passwordEncoder.encode("1234"))
                        .setRoles(setRoles);
                userRepository.save(admin);
            }
        });
    }
}
