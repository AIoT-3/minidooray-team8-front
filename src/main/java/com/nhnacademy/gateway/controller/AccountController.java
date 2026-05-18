package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.dto.account.LoginRequest;
import com.nhnacademy.gateway.dto.account.SignupRequest;
import com.nhnacademy.gateway.dto.account.UserStatusUpdateRequest;
import com.nhnacademy.gateway.service.AccountApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountApiService accountApiService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("request", new SignupRequest(null, null, null));
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@Valid @ModelAttribute SignupRequest request) {
        accountApiService.signup(request);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("request", new LoginRequest(null, null));
        return "login";
    }

    @PostMapping("/users/{userId}/status")
    public String updateUserStatus(@PathVariable String userId, @Valid @ModelAttribute UserStatusUpdateRequest request) {
        accountApiService.updateUserStatus(userId, request);
        return "redirect:/my-projects";
    }
}

