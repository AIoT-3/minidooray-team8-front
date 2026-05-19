package com.nhnacademy.front.controller;

import com.nhnacademy.front.dto.task.CommentCreateRequest;
import com.nhnacademy.front.service.CommentApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks/{task-id}/comments")
public class CommentController {

    private final CommentApiService commentApiService;

    @PostMapping
    public String createComment(@PathVariable("project-id") Long projectId,
                                @PathVariable("task-id") Long taskId,
                                @Valid @ModelAttribute CommentCreateRequest request) {
        commentApiService.createComment(projectId, taskId, request);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{comment-id}/edit")
    public String updateComment(@PathVariable("project-id") Long projectId,
                                @PathVariable("task-id") Long taskId,
                                @PathVariable("comment-id") Long commentId,
                                @Valid @ModelAttribute CommentCreateRequest request) {
        commentApiService.updateComment(projectId, taskId, commentId, request);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{comment-id}/delete")
    public String deleteComment(@PathVariable("project-id") Long projectId,
                                @PathVariable("task-id") Long taskId,
                                @PathVariable("comment-id") Long commentId) {
        commentApiService.deleteComment(projectId, taskId, commentId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

}
