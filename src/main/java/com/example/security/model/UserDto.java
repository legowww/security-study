package com.example.security.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String role;
    private String provider;
    private String providerId;


    public static UserDto fromEntity(UserEntity entity) {
        return new UserDto(
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getRole(),
                entity.getProvider(),
                entity.getProviderId()
        );
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", provider='" + provider + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
