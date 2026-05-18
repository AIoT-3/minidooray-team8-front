package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.dto.task.CommentCreateRequest;
import com.nhnacademy.gateway.service.CommentApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    private final CommentApiService commentApiService;

    @PostMapping
    public String createComment(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @Valid @ModelAttribute CommentCreateRequest request) {
        commentApiService.createComment(projectId, taskId, request);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{commentId}/edit")
    public String updateComment(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long commentId,
                                @Valid @ModelAttribute CommentCreateRequest request) {
        commentApiService.updateComment(projectId, taskId, commentId, request);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long commentId) {
        commentApiService.deleteComment(projectId, taskId, commentId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

}
