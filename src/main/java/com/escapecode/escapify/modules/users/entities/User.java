package com.escapecode.escapify.modules.users.entities;

import com.escapecode.escapify.shared.model.Audit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String lastname;

    @Column(length = 20, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    private String photoUrl;

    @Column(length = 10, nullable = false)
    private String provider; // ESCAPIFY, GOOGLE, FACEBOOK

    private String providerId;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean deleted;

    @Embedded
    private Audit audit = new Audit();

}
