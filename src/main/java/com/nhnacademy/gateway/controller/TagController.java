package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.dto.task.TagCreateRequest;
import com.nhnacademy.gateway.service.TagApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tags")
public class TagController {

    private final TagApiService tagApiService;

    @GetMapping("/new")
    public String tagForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("request", new TagCreateRequest(null));
        return "tag-form";
    }

    @PostMapping
    public String createTag(@PathVariable Long projectId, @ModelAttribute TagCreateRequest request) {
        tagApiService.createTag(projectId, request);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{tagId}/edit")
    public String tagUpdateForm(@PathVariable Long projectId, @PathVariable Long tagId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("tagId", tagId);
        return "tag-edit-form";
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
