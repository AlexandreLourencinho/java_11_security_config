package com.example.springsecurityauthtwo.security.repositories;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
