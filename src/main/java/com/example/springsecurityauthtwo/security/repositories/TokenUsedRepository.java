package com.example.springsecurityauthtwo.security.repositories;

import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.entities.TokenUsed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenUsedRepository extends CrudRepository<TokenUsed, Long> {
    Optional<TokenUsed> findByUser(AppUser user);
}
