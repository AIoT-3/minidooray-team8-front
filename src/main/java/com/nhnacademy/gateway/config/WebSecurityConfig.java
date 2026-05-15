package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.service.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // 로그인, 회원가입 관련 정적 리소스 및 API는 인증 없이 허용
                .requestMatchers("/accounts/login", "/accounts/signup", "/static/**").permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
        )
                .formLogin(form -> form
                        .loginPage("/accounts/login") // 커스텀 로그인 페이지 경로
                        .loginProcessingUrl("/accounts/login") // Spring Security가 POST 로그인을 처리할 URL
                        .usernameParameter("userId") // 폼 데이터의 ID 필드명
                        .passwordParameter("password") // 폼 데이터의 PW 필드명
                        .defaultSuccessUrl("/") // 성공 시 이동할 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/accounts/logout")
                        .logoutSuccessUrl("/accounts/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION")
                )
                .csrf(csrf -> csrf.disable()); // 개발 단계에서는 CSRF 비활성화 (필요 시 나중에 설정)

        return http.build();
    }

    /**
     * 커스텀 인증 Provider 등록
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
