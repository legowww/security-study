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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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


    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication.getPrincipal()={}", principal.getUser());
        log.info("userDetails={}", principalDetails.getUser());

        return "세션정보활용하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOauthLogin(Authentication authentication,
                                 @AuthenticationPrincipal OAuth2User user) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authentication.getPrincipal()={}", oAuth2User.getAttributes());
        log.info("user={}", user.getAttributes());

        return "Oauth 세션정보활용하기";
    }


    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("{}", principalDetails.getUser());
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
        userEntityRepository.save(UserEntity.of(userDto.getUsername(), encPassword, userDto.getEmail(), userDto.getRole(), userDto.getProvider(), userDto.getProviderId()));
        return "redirect:/loginForm";
    }


    @GetMapping("/info")
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public String info() {
        return "개인정보";
    }
 }
