package com.example.security.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //super.loadUser(userRequest).getAttributes() 여기에 내가 필요한 토큰 등 필요한 정보가 다 들어있다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest={}", userRequest.getClientRegistration()); //registrationId 로 어떤 OAuth 로 로그인 했는지 확인가능.

        /**
         * sub: 101524744306866890761 (Primary Key 개념)
         * name: 박형진
         * given_name: 형진
         * family_name: 박
         * picture: https://lh3.googleusercontent.com/a/AEdFTp4VEGxWWls-r3D3hGCkCFjqPKl0uP-XG30Qpoux=s96-c
         * email: legowww@gmail.com (회원가입시 사용)
         * email_verified: true
         * locale: ko
         *
         * getAttributes() 정보를 활용하여 회원가입:
         * username="google_101524744306866890761" -> 유일성 보장
         * password="암호화(겟인데어)"              -> 내가 직접 치는 것이 아니기 때문에 null 만 아니면 된다.
         * email="legowww@gmail.com"
         * role="ROLE_USER"
         * provider="google"
         * providerId="101524744306866890761"
         */
        //구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code 를 리턴(Oauth-Client 라이브러리가 받음) -> AccessToken 요청 -> userRequest 정보 완성
        //userRequest 정보 -> 회원 프로필 받아야함(loadUser() 메서드를 통해) -> 회원 프로필 완성
        OAuth2User oAuth2User = super.loadUser(userRequest);



        return super.loadUser(userRequest);
    }
}



//        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
//        for (String s : attributes.keySet()) {
//            System.out.println(s + ": " + attributes.get(s));
//        }