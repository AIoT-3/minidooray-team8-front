package com.nhnacademy.front.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void testCustomUserDetails_Properties() {
        CustomUserDetails userDetails = new CustomUserDetails("user1");

        assertEquals("user1", userDetails.getUsername());
        assertNull(userDetails.getPassword());
        assertFalse(userDetails.getAuthorities().isEmpty());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
