package com.nhnacademy.front.controller;

import com.nhnacademy.front.dto.task.MilestoneCreateRequest;
import com.nhnacademy.front.dto.task.MilestoneDetailDto;
import com.nhnacademy.front.service.MilestoneApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/milestones")
public class MilestoneController {

    private final MilestoneApiService milestoneApiService;

    @PostMapping
    public String createMilestone(@PathVariable("project-id") Long projectId, @Valid @ModelAttribute MilestoneCreateRequest request) {
        milestoneApiService.createMilestone(projectId, request);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{milestone-id}")
    @ResponseBody
    public MilestoneDetailDto getMilestone(@PathVariable("project-id") Long projectId, @PathVariable("milestone-id") Long milestoneId) {
        return milestoneApiService.getMilestone(projectId, milestoneId);
    }

    @PostMapping("/{milestone-id}/edit")
    public String updateMilestone(@PathVariable("project-id") Long projectId, @PathVariable("milestone-id") Long milestoneId, @Valid @ModelAttribute MilestoneCreateRequest request) {
        milestoneApiService.updateMilestone(projectId, milestoneId, request);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{milestone-id}/delete")
    public String deleteMilestone(@PathVariable("project-id") Long projectId, @PathVariable("milestone-id") Long milestoneId) {
        milestoneApiService.deleteMilestone(projectId, milestoneId);
        return "redirect:/projects/" + projectId;
    }
}
