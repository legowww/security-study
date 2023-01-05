package com.example.security.config.oauth;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.oauth.provider.GoogleUserInfo;
import com.example.security.config.oauth.provider.NaverUserInfo;
import com.example.security.config.oauth.provider.Oauth2UserInfo;
import com.example.security.model.UserDto;
import com.example.security.model.UserEntity;
import com.example.security.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

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

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserEntityRepository userEntityRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 메서드
    //구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code 를 리턴(Oauth-Client 라이브러리가 받음) -> AccessToken 요청 -> userRequest 정보 완성
    //userRequest 정보 -> 회원 프로필 받아야함(loadUser() 메서드를 통해) -> 회원 프로필 완성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes:{}", oAuth2User.getAttributes());

        Oauth2UserInfo oauth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oauth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }

        String role = "ROLE_USER";
        String email = oauth2UserInfo.getEmail();
        String provider = oauth2UserInfo.getProvider();
        String providerId =  oauth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("비밀번호");

        UserEntity userEntity = null;
        //이미 가입된 회원이라면
        boolean present = userEntityRepository.existsByUsername(username);
        if (userEntityRepository.findByUsername(username).isPresent()) {
            userEntity = userEntityRepository.findByUsername(username).get();
        } else {
            userEntity = userEntityRepository.save(UserEntity.of(username, password, email, role, provider, providerId));
        }

        //리턴값이 Authentication 에 들어가서 Authentication(PrincipalDetails(useDto, attributes))를 만들고
        //Authentication 이 시큐리티 session 에 저장된다.
        //이 작업을 loadUser 가 수행한다.
        return new PrincipalDetails(UserDto.fromEntity(userEntity), oAuth2User.getAttributes());
    }
}
