package com.example.security.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSaveRequest {
    private String username;
    private String password;
    private String email;
}
