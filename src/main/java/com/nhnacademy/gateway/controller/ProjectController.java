package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.dto.task.ProjectCreateRequest;
import com.nhnacademy.gateway.dto.task.ProjectDto;
import com.nhnacademy.gateway.dto.task.ProjectMemberRequest;
import com.nhnacademy.gateway.service.AccountApiService;
import com.nhnacademy.gateway.service.ProjectApiService;
import com.nhnacademy.gateway.service.TagApiService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.nhnacademy.gateway.dto.task.ProjectUpdateRequest;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectApiService projectApiService;
    private final AccountApiService accountApiService;
    private final TagApiService tagApiService;

    public ProjectController(ProjectApiService projectApiService, AccountApiService accountApiService, TagApiService tagApiService) {
        this.projectApiService = projectApiService;
        this.accountApiService = accountApiService;
        this.tagApiService = tagApiService;
    }

    @GetMapping("/{projectId}")
    public String getProject(@PathVariable Long projectId, Model model) {
        model.addAttribute("project", projectApiService.getProjectDetail(projectId));
        model.addAttribute("tags", tagApiService.getTags(projectId));
        return "project-detail";
    }

    @PostMapping
    public String createProject(@Valid @ModelAttribute ProjectCreateRequest request) {
        projectApiService.createProject(request);
        return "redirect:/my-projects"; 
    }

    @PostMapping("/{projectId}/edit")
    public String updateProject(@PathVariable Long projectId, @Valid @ModelAttribute ProjectUpdateRequest request) {
        projectApiService.updateProject(projectId, request);
        return "redirect:/projects/" + projectId; 
    }

    @PostMapping("/{projectId}/close")
    public String closeProject(@PathVariable Long projectId) {
        ProjectUpdateRequest closeRequest = new ProjectUpdateRequest(null, "CLOSED");
        projectApiService.updateProject(projectId, closeRequest);
        return "redirect:/my-projects";
    }

    @PostMapping("/{projectId}/members")
    public String addProjectMember(@PathVariable Long projectId, @Valid @ModelAttribute ProjectMemberRequest request) {
        // 1. 유저 존재 여부 확인 (Account API 호출)
        accountApiService.getUser(request.userId());
        
        // 2. 존재할 경우 멤버 추가 진행 (Task API 호출)
        projectApiService.addProjectMember(projectId, request);
        return "redirect:/projects/" + projectId;
    }
}
