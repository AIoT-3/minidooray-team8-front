package com.nhnacademy.front.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class WebConfig {

    @Value("${minidooray.gateway.url}")
    private String gatewayUrl;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(gatewayUrl)
                .additionalInterceptors(new SessionCookieInterceptor())
                .build();
    }

    private static class SessionCookieInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                HttpServletRequest currentRequest = attributes.getRequest();
                Cookie[] cookies = currentRequest.getCookies();
                
                if (cookies != null) {
                    Arrays.stream(cookies)
                            .filter(cookie -> "SESSION".equals(cookie.getName()))
                            .findFirst()
                            .ifPresent(sessionCookie -> 
                                    request.getHeaders().add("Cookie", "SESSION=" + sessionCookie.getValue())
                            );
                }
            }

            return execution.execute(request, body);
        }
    }
}
