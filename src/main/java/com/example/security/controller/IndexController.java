package com.example.security.controller;

import com.example.security.model.UserEntity;
import com.example.security.model.UserSaveRequest;
import com.example.security.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final UserEntityRepository userEntityRepository;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(@ModelAttribute("user") UserSaveRequest request) {
        return "joinForm";
    }

    @PostMapping("/join")
    @ResponseBody
    public String join(@ModelAttribute UserSaveRequest request) {
        userEntityRepository.save(UserEntity.of(request.getUsername(), request.getPassword(), request.getEmail(), "ROLE_USER"));
        return "join";
    }
}
