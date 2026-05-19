package com.nhnacademy.front.controller;

import com.nhnacademy.front.dto.task.TagCreateRequest;
import com.nhnacademy.front.service.TagApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tags")
public class TagController {

    private final TagApiService tagApiService;

    @PostMapping
    public String createTag(@PathVariable("project-id") Long projectId, @Valid @ModelAttribute TagCreateRequest request) {
        tagApiService.createTag(projectId, request);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{tag-id}/edit")
    public String updateTag(@PathVariable("project-id") Long projectId, @PathVariable("tag-id") Long tagId, @Valid @ModelAttribute TagCreateRequest request) {
        tagApiService.updateTag(projectId, tagId, request);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{tag-id}/delete")
    public String deleteTag(@PathVariable("project-id") Long projectId, @PathVariable("tag-id") Long tagId) {
        tagApiService.deleteTag(projectId, tagId);
        return "redirect:/projects/" + projectId;
    }
}
