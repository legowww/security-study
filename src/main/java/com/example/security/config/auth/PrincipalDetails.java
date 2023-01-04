package com.example.security.config.auth;

import com.example.security.model.UserDto;
import com.example.security.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


/**
 * 시큐리티가 /login 낚아채서 로그인을 진행시킨다.
 * 이때 로그인이 완료가 되면 session 을 만들어서 넣어준다.
 * Security 는 Security Context Holder 라는 공간에 유저 정보를 저장한다.
 * 저장할 때, KEY 값에 session 정보를 저장한다.
 * 이 session 에 들어갈 수 있는 객체는 정해져 있다 => Authentication 타입 객체
 * Authentication 안에 User 정보가 있어야 내가 활용할 수 있다.
 *
 * 정리:
 * Security Session 에 session 정보를 저장한다.
 * 여기 들어올 수 있는 객체는 Authentication 뿐이다.
 * Authentication 안에 유저 정보를 저장하려면 UserDetails 타입으로 저장해야만 한다.
 * @Controller 에서 Authentication 에 담긴 UserDetails 를 꺼내면 내가 저장한 User 정보를 활용할 수 있게 된다.
 *
 * PrincipalDetails 는 UserDetails 를 상속했기 때문에 Authentication 에 넣을 수 있다.
 *
 * session <= Authentication(UserDetails)
 */

public class PrincipalDetails implements UserDetails {
    //컴포지션으로 User(또는 DTO) 사용
    private UserDto user;

    public PrincipalDetails(UserDto user) {
        this.user = user;
    }

    //해당 User 의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect =  new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //우리 사이트가 1년동안 회원이 로그인을 안하면 휴면계정으로 하기로 했다면
        // user.getLoginDate() 가져와서 현재 시간과 비교하여 1년 초과하면 false 반환
        return true;
    }
}
