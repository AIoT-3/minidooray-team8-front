package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.dto.task.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.task.MilestoneDetailDto;
import com.nhnacademy.gateway.service.MilestoneApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/milestones")
public class MilestoneController {

    private final MilestoneApiService milestoneApiService;

    @PostMapping
    public String createMilestone(@PathVariable Long projectId, @ModelAttribute MilestoneCreateRequest request) {
        milestoneApiService.createMilestone(projectId, request);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{milestoneId}")
    @ResponseBody
    public MilestoneDetailDto getMilestone(@PathVariable Long projectId, @PathVariable Long milestoneId) {
        return milestoneApiService.getMilestone(projectId, milestoneId);
    }

    @PostMapping("/{milestoneId}/edit")
    public String updateMilestone(@PathVariable Long projectId, @PathVariable Long milestoneId, @ModelAttribute MilestoneCreateRequest request) {
        milestoneApiService.updateMilestone(projectId, milestoneId, request);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{milestoneId}/delete")
    public String deleteMilestone(@PathVariable Long projectId, @PathVariable Long milestoneId) {
        milestoneApiService.deleteMilestone(projectId, milestoneId);
        return "redirect:/projects/" + projectId;
    }
}
