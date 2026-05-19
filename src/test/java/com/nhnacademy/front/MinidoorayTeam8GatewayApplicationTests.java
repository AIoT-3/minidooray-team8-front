package com.nhnacademy.front;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MinidoorayTeam8GatewayApplicationTests {

    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

}
