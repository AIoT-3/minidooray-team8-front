package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.account.LoginRequest;
import com.nhnacademy.front.dto.account.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Account API를 사용하여 사용자 인증을 수행하는 CustomAuthenticationProvider입니다.
 * Spring Security의 기본 인증 흐름을 가로채서 외부 API와 통신합니다.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountApiService accountApiService;

    /**
     * 인증 로직을 수행합니다.
     * @param authentication 사용자가 입력한 아이디와 비밀번호 정보가 담긴 토큰
     * @return 인증이 완료된 Authentication 객체 (SecurityContext에 저장됨)
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        String password = (String) authentication.getCredentials();

        try {
            // Account API를 호출하여 아이디와 비밀번호가 일치하는지 확인
            // 성공 시 userId가 담긴 LoginResponse가 반환됩니다.
            LoginResponse response = accountApiService.login(new LoginRequest(userId, password));

            if (response != null && response.userId() != null) {
                // 인증에 성공했다면 CustomUserDetails 객체 생성
                CustomUserDetails userDetails = new CustomUserDetails(response.userId());

                // 인증된 토큰을 생성하여 반환
                // 이때 권한(Authorities) 정보도 함께 넘겨주어 '인증됨' 상태로 만듭니다.
                return new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
            }
        } catch (Exception e) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        throw new BadCredentialsException("인증 서비스에 문제가 발생했습니다.");
    }

    /**
     * 이 Provider가 어떤 타입의 Authentication 객체를 지원하는지 설정합니다.
     * UsernamePasswordAuthenticationToken 타입을 지원하도록 설정합니다.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
