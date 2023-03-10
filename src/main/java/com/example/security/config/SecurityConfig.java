package com.example.security.config;


import com.example.security.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true) // secured 어노테이션 활성화 -> @Secured("ROLE_ADMIN") 붙은 메서드
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOauth2UserService principalOauth2UserService;

    /**
     * http.csrf().disable() ->
                서버가 응답한 html 페이지로 요청했는지
                아니면 강제로 포스트맨.같은걸로 요청했는지를 검증하는데 사용되요.
                그거 해제안하면 포스트맨 테스트가 안되요
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers("/user/**").authenticated()
                        .mvcMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .mvcMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll()
                )
                .formLogin()
                    .loginPage("/loginForm") //authenticated 되기 위해 로그인이 필요할 경우 해당 url 로 이동
                    .loginProcessingUrl("/login") // "/login" 주소로 post 요청이 날아가면 Security 가 낚아채서 대신 로그인 진행
                    .defaultSuccessUrl("/") //Security 에서 로그인 성공하면 해당 url 로 이동
                .and()
                .oauth2Login()
                    .loginPage("/loginForm") //authenticated 되기 위해 로그인이 필요할 경우 해당 url 로 이동, 구글 로그인 완료된 뒤 후처리 필요. 액세스토큰+사용자프로필정보 받음
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService) //principalOauth2UserService 의 loadUser() 메서드에서 후처리
                    .and()
                .and()
                .build();
    }
}
