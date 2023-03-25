package com.example.springsecurityauthtwo.security.model.entities;

import com.example.springsecurityauthtwo.security.model.enumeration.ERole;

import java.util.Objects;
import javax.persistence.*;

import lombok.*;
import org.hibernate.Hibernate;
import lombok.experimental.Accessors;

/**
 * Model / entity type for users' roles
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Accessors(chain = true)
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private ERole name;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppRole appRole = (AppRole) o;
        return id != null && Objects.equals(id, appRole.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
