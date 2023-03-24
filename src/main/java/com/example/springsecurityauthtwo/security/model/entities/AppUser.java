package com.example.springsecurityauthtwo.security.model.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;


/**
 * User entity / model
 * @author Alexandre Lourencinho
 * @version 1.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
@Accessors(chain = true)
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private Set<AppRole> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUser appUser = (AppUser) o;
        return id != null && Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
