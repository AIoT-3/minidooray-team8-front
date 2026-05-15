package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.dto.task.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.task.MilestoneDto;
import com.nhnacademy.gateway.dto.task.TagCreateRequest;
import com.nhnacademy.gateway.dto.task.TagDto;
import com.nhnacademy.gateway.dto.task.TaskCreateRequest;
import com.nhnacademy.gateway.dto.task.TaskDetailDto;
import com.nhnacademy.gateway.dto.task.TaskDto;
import com.nhnacademy.gateway.dto.task.TaskMilestoneRequest;
import com.nhnacademy.gateway.dto.task.TaskTagRequest;
import com.nhnacademy.gateway.dto.task.TaskUpdateRequest;
import com.nhnacademy.gateway.service.MilestoneApiService;
import com.nhnacademy.gateway.service.ProjectApiService;
import com.nhnacademy.gateway.service.TagApiService;
import com.nhnacademy.gateway.service.TaskApiService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {
    
    private final TaskApiService taskApiService;
    private final MilestoneApiService milestoneApiService;
    private final TagApiService tagApiService;
    private final ProjectApiService projectApiService;

    @GetMapping("/new")
    public String taskCreateForm(@PathVariable("projectId") Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("milestones", milestoneApiService.getMilestones(projectId));
        model.addAttribute("tags", tagApiService.getTags(projectId));
        model.addAttribute("request", new TaskCreateRequest(null, projectId, null, null, null, null));
        return "task-form";
    }

    @PostMapping
    public String createTask(@PathVariable("projectId") Long projectId, 
                             @ModelAttribute TaskCreateRequest request,
                             @RequestParam(required = false) Long milestoneId,
                             @RequestParam(required = false) String newMilestoneName,
                             @RequestParam(required = false) LocalDate newMilestoneStartDate,
                             @RequestParam(required = false) LocalDate newMilestoneEndDate,
                             @RequestParam(required = false) List<Long> tagIds,
                             @RequestParam(required = false) String newTagName) {
        
        TaskDto createdTask = taskApiService.createTask(request);

        // Milestone handle
        if (newMilestoneName != null && !newMilestoneName.isBlank()) {
            MilestoneDto newMilestone = milestoneApiService.createMilestone(projectId, new MilestoneCreateRequest(newMilestoneName, newMilestoneStartDate, newMilestoneEndDate));
            milestoneId = newMilestone.milestoneId();
        }
        if (milestoneId != null) {
            taskApiService.setMilestone(createdTask.taskId(), new TaskMilestoneRequest(milestoneId));
        }

        // Tags handle
        List<Long> finalTagIds = tagIds != null ? new ArrayList<>(tagIds) : new ArrayList<>();
        if (newTagName != null && !newTagName.isBlank()) {
            TagDto newTag = tagApiService.createTag(projectId, new TagCreateRequest(newTagName));
            finalTagIds.add(newTag.tagId());
        }
        if (!finalTagIds.isEmpty()) {
            taskApiService.addTags(createdTask.taskId(), new TaskTagRequest(finalTagIds));
        }

        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{taskId}")
    public String getTask(Model model, @PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("task", taskApiService.getTask(taskId));
        model.addAttribute("project", projectApiService.getProjectDetail(projectId));
        model.addAttribute("tags", tagApiService.getTags(projectId));
        return "task-detail";
    }

    @GetMapping("/{taskId}/edit")
    public String taskUpdateForm(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId, Model model) {
        TaskDetailDto task = taskApiService.getTask(taskId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("milestones", milestoneApiService.getMilestones(projectId));
        model.addAttribute("request", new TaskUpdateRequest(task.title(), task.content()));
        return "task-edit-form";
    }

    @PostMapping("/{taskId}/edit")
    public String updateTask(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId, @ModelAttribute TaskUpdateRequest request) {
        taskApiService.updateTask(taskId, request);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{taskId}/delete")
    public String deleteTask(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
        taskApiService.deleteTask(taskId);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{taskId}/milestones")
    public String setMilestone(@PathVariable("projectId") Long projectId, 
                               @PathVariable("taskId") Long taskId, 
                               @RequestParam(required = false) Long milestoneId,
                               @RequestParam(required = false) String newMilestoneName,
                               @RequestParam(required = false) LocalDate newMilestoneStartDate,
                               @RequestParam(required = false) LocalDate newMilestoneEndDate) {
        
        if (newMilestoneName != null && !newMilestoneName.isBlank()) {
            MilestoneDto newMilestone = milestoneApiService.createMilestone(projectId, new MilestoneCreateRequest(newMilestoneName, newMilestoneStartDate, newMilestoneEndDate));
            milestoneId = newMilestone.milestoneId();
        }
        
        taskApiService.setMilestone(taskId, new TaskMilestoneRequest(milestoneId));
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{taskId}/tags")
    public String addTags(@PathVariable("projectId") Long projectId, 
                          @PathVariable("taskId") Long taskId, 
                          @RequestParam(required = false) List<Long> tagIds,
                          @RequestParam(required = false) String newTagName) {
        
        List<Long> finalTagIds = tagIds != null ? new ArrayList<>(tagIds) : new ArrayList<>();
        if (newTagName != null && !newTagName.isBlank()) {
            TagDto newTag = tagApiService.createTag(projectId, new TagCreateRequest(newTagName));
            finalTagIds.add(newTag.tagId());
        }
        
        taskApiService.addTags(taskId, new TaskTagRequest(finalTagIds));
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }
}
