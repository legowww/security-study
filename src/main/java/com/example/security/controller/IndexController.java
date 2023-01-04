package com.example.security.controller;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.model.UserEntity;
import com.example.security.model.UserDto;
import com.example.security.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        log.info("authentication.getPrincipal()={}", authentication.getPrincipal());
        log.info("principal.getUsername()={}", principal.getUsername());
        log.info("principal.getPassword()={}", principal.getPassword());
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
    public String joinForm(@ModelAttribute("user") UserDto userDto) {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserDto userDto) {
        userDto.setRole("ROLE_USER");

        String encPassword = bCryptPasswordEncoder.encode(userDto.getPassword());
        userEntityRepository.save(UserEntity.of(userDto.getUsername(), encPassword, userDto.getEmail(), userDto.getRole()));
        return "redirect:/loginForm";
    }


    @GetMapping("/info")
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public String info() {
        return "개인정보";
    }
 }
