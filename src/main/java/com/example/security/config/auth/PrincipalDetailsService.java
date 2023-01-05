package com.example.security.config.auth;

import com.example.security.model.UserDto;
import com.example.security.model.UserEntity;
import com.example.security.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 시큐리티 설정에서 loginProcessingUrl("/login") 를 설정했기 때문에
 *
 * 1. 로그인 폼에서 /login action
 * 2. 스프링 컨테이너는 UserDetailsService(=PrincipalDetailsService) 로 등록된 타입을 찾는다.
 * 3. UserDetailsService 는 자동으로 loadUserByUsername 메서드를 실행시킨다.
 * 4. 이때 로그인 폼에서 username 으로 넘어온 파라미터를 loadUserByUsername 에 매칭시킨다.
 */
@Service // @컴포넨트 했기 때문에 PrincipalDetailsService 가 자동으로 등록된다.
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    //로그인 폼에서 input 으로 사용되는 username 은 loadUserByUsername 의 username 변수명과 동일해야 발동한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username={}", username);
        UserDto userDto = userEntityRepository.findByUsername(username).map(UserDto::fromEntity).orElseThrow(() ->
                new UsernameNotFoundException("user not founded"));

        //리턴값이 Authentication 에 들어가서 Authentication(PrincipalDetails(useDto))를 만들고
        //Authentication 이 시큐리티 session 에 저장된다.
        //이 작업을 loadUserByUsername 가 수행한다.
        return new PrincipalDetails(userDto);
    }
}
