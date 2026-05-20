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
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountApiService accountApiService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        String password = (String) authentication.getCredentials();

        try {
            LoginResponse response = accountApiService.login(new LoginRequest(userId, password));

            if (response != null && response.userId() != null) {
                CustomUserDetails userDetails = new CustomUserDetails(response.userId());

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

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
