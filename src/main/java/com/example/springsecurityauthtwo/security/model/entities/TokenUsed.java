package com.example.springsecurityauthtwo.security.model.entities;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Accessors(chain = true)
public class TokenUsed {

    @Id
    @Column(name = "user_id")
    private Long id;
    private String accessToken;
    private String refreshToken;
    private int refreshTokenAccount;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private AppUser user;
    private Date emitDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TokenUsed tokenUsed = (TokenUsed) o;
        return id != null && Objects.equals(id, tokenUsed.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
