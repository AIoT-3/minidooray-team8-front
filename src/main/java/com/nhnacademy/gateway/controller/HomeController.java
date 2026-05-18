package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.service.ProjectApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProjectApiService projectApiService;

    @GetMapping("/my-projects")
    public String index(Model model) {
        model.addAttribute("projects", projectApiService.getProjects());
        return "index";
    }
}
