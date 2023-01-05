package com.example.security.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Setter private String username;
    @Setter private String password;
    @Setter private String email;
    @Setter private String role;
    @Setter private String provider;
    @Setter private String providerId;
    @Setter @CreationTimestamp private Timestamp createDate;

    protected UserEntity() {}

    public static UserEntity of(String username, String password, String email, String role, String provider, String providerId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        userEntity.setRole(role);
        userEntity.setProvider(provider);
        userEntity.setProviderId(providerId);
        return userEntity;
    }
}
