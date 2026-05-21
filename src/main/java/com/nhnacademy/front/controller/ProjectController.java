package com.nhnacademy.front.controller;

import com.nhnacademy.front.dto.task.ProjectCreateRequest;
import com.nhnacademy.front.dto.task.ProjectDetailDto;
import com.nhnacademy.front.dto.task.ProjectMemberRequest;
import com.nhnacademy.front.dto.task.TaskDto;
import com.nhnacademy.front.service.AccountApiService;
import com.nhnacademy.front.service.ProjectApiService;
import com.nhnacademy.front.service.TagApiService;
import com.nhnacademy.front.service.TaskApiService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.nhnacademy.front.dto.task.ProjectUpdateRequest;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectApiService projectApiService;
    private final AccountApiService accountApiService;
    private final TagApiService tagApiService;
    private final TaskApiService taskApiService;

    public ProjectController(ProjectApiService projectApiService, AccountApiService accountApiService, TagApiService tagApiService, TaskApiService taskApiService) {
        this.projectApiService = projectApiService;
        this.accountApiService = accountApiService;
        this.tagApiService = tagApiService;
        this.taskApiService = taskApiService;
    }

    @GetMapping("/{project-id}")
    public String getProject(@PathVariable("project-id") Long projectId, @RequestParam(required = false) Long tagId, Model model) {
        ProjectDetailDto project = projectApiService.getProjectDetail(projectId);
        
        if (tagId != null) {
            List<TaskDto> filteredTasks = taskApiService.getTasksByTag(projectId, tagId);
            project = new ProjectDetailDto(
                    project.projectId(), 
                    project.name(), 
                    project.status(), 
                    project.adminId(), 
                    project.members(), 
                    filteredTasks, 
                    project.milestones()
            );
        }
        
        model.addAttribute("project", project);
        model.addAttribute("tags", tagApiService.getTags(projectId));
        return "project-detail";
    }

    @PostMapping
    public String createProject(@Valid @ModelAttribute ProjectCreateRequest request) {
        projectApiService.createProject(request);
        return "redirect:/my-projects"; 
    }

    @PostMapping("/{project-id}/edit")
    public String updateProject(@PathVariable("project-id") Long projectId, @Valid @ModelAttribute ProjectUpdateRequest request) {
        projectApiService.updateProject(projectId, request);
        return "redirect:/projects/" + projectId; 
    }

    @PostMapping("/{project-id}/close")
    public String closeProject(@PathVariable("project-id") Long projectId) {
        projectApiService.closeProject(projectId);
        return "redirect:/my-projects";
    }

    @PostMapping("/{project-id}/members")
    public String addProjectMember(@PathVariable("project-id") Long projectId, @Valid @ModelAttribute ProjectMemberRequest request) {
        // 1. 유저 존재 여부 확인 (Account API 호출)
        accountApiService.getUser(request.userId());
        
        // 2. 존재할 경우 멤버 추가 진행 (Task API 호출)
        projectApiService.addProjectMember(projectId, request);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{project-id}/members/{user-id}/delete")
    public String deleteProjectMember(@PathVariable("project-id") Long projectId, @PathVariable("user-id") String userId, java.security.Principal principal) {
        projectApiService.deleteProjectMember(projectId, userId);
        
        // 만약 본인이 탈퇴한 경우 대시보드로 이동
        if (principal != null && principal.getName().equals(userId)) {
            return "redirect:/my-projects";
        }
        
        return "redirect:/projects/" + projectId;
    }
}
