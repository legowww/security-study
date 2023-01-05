package com.example.security.config.oauth.provider;

import java.util.Map;

/**
 * getAttributes:{resultcode=00, message=success, response={id=6JkSBYJXJGgVoco_Tzc40lG6VYz9qJZyTMaGsPA7us0, email=gmzx@naver.com, name=박형진}}
 * 에서 response 내부 값 뽑아내기
 * =>
 *             oauth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
 *
 *
 *             response={id=6JkSBYJXJGgVoco_Tzc40lG6VYz9qJZyTMaGsPA7us0, email=gmzx@naver.com, name=박형진}
 */
public class NaverUserInfo implements Oauth2UserInfo{

    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
