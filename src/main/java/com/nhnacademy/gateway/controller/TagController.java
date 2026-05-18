package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.dto.task.TagCreateRequest;
import com.nhnacademy.gateway.service.TagApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tags")
public class TagController {

    private final TagApiService tagApiService;

    @PostMapping
    public String createTag(@PathVariable Long projectId, @ModelAttribute TagCreateRequest request) {
        tagApiService.createTag(projectId, request);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{tagId}/edit")
    public String updateTag(@PathVariable Long projectId, @PathVariable Long tagId, @ModelAttribute TagCreateRequest request) {
        tagApiService.updateTag(projectId, tagId, request);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{tagId}/delete")
    public String deleteTag(@PathVariable Long projectId, @PathVariable Long tagId) {
        tagApiService.deleteTag(projectId, tagId);
        return "redirect:/projects/" + projectId;
    }
}
