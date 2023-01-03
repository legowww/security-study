package com.example.security.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String role;

    public static UserDto fromEntity(UserEntity entity) {
        return new UserDto(
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getRole()
        );
    }
}
