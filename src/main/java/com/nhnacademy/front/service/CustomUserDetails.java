package com.nhnacademy.front.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security의 인증된 사용자 정보를 담는 CustomUserDetails 클래스입니다.
 * UserDetails 인터페이스를 구현하여 SecurityContext에 사용자 식별자(userId)를 저장할 수 있게 합니다.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final String userId;

    public CustomUserDetails(String userId) {
        this.userId = userId;
    }

    /**
     * 사용자가 가진 권한(Authority) 목록을 반환합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     */
    @Override
    public String getPassword() {
        return null; 
    }

    /**
     * 사용자의 아이디(username)를 반환합니다.
     * 여기서는 userId를 username으로 사용합니다.
     */
    @Override
    public String getUsername() {
        return userId;
    }

    /**
     * 계정 만료 여부를 반환합니다. (true: 만료되지 않음)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부를 반환합니다. (true: 잠기지 않음)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명(비밀번호 등) 만료 여부를 반환합니다. (true: 만료되지 않음)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부를 반환합니다. (true: 활성화됨)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
