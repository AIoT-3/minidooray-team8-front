package com.nhnacademy.front.controller;

import com.nhnacademy.front.dto.task.ProjectDto;
import com.nhnacademy.front.service.ProjectApiService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProjectApiService projectApiService;

    @GetMapping("/my-projects")
    public String index(@RequestParam(required = false, defaultValue = "ALL") String status, Model model) {
        List<ProjectDto> projects = projectApiService.getProjects();
        
        if (!"ALL".equalsIgnoreCase(status)) {
            projects = projects.stream()
                             .filter(p -> p.status().equalsIgnoreCase(status))
                             .collect(Collectors.toList());
        }
        
        model.addAttribute("projects", projects);
        model.addAttribute("currentStatus", status.toUpperCase());
        return "index";
    }
}
