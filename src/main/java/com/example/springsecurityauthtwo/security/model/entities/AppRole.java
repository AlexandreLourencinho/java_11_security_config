package com.example.springsecurityauthtwo.security.model.entities;


import com.example.springsecurityauthtwo.security.model.enumeration.ERole;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Accessors(chain = true)
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ERole name;
    private String description;

}
