package com.nhnacademy.front.controller;

import com.nhnacademy.front.dto.task.MilestoneCreateRequest;
import com.nhnacademy.front.dto.task.MilestoneDto;
import com.nhnacademy.front.dto.task.TagCreateRequest;
import com.nhnacademy.front.dto.task.TagDto;
import com.nhnacademy.front.dto.task.TaskCreateRequest;
import com.nhnacademy.front.dto.task.TaskDto;
import com.nhnacademy.front.dto.task.TaskMilestoneRequest;
import com.nhnacademy.front.dto.task.TaskTagRequest;
import com.nhnacademy.front.dto.task.TaskUpdateRequest;
import com.nhnacademy.front.service.MilestoneApiService;
import com.nhnacademy.front.service.ProjectApiService;
import com.nhnacademy.front.service.TagApiService;
import com.nhnacademy.front.service.TaskApiService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project-id}/tasks")
public class TaskController {
    
    private final TaskApiService taskApiService;
    private final MilestoneApiService milestoneApiService;
    private final TagApiService tagApiService;
    private final ProjectApiService projectApiService;

    @GetMapping
    @ResponseBody
    public List<TaskDto> getTasks(@PathVariable("project-id") Long projectId, @RequestParam(required = false) Long tagId) {
        if (tagId == null) {
            return projectApiService.getProjectDetail(projectId).tasks();
        }
        return taskApiService.getTasksByTag(projectId, tagId);
    }

    @PostMapping
    public String createTask(@PathVariable("project-id") Long projectId, 
                             @Valid @ModelAttribute TaskCreateRequest request,
                             @RequestParam(required = false) Long milestoneId,
                             @RequestParam(required = false) String newMilestoneName,
                             @RequestParam(required = false) LocalDate newMilestoneStartDate,
                             @RequestParam(required = false) LocalDate newMilestoneEndDate,
                             @RequestParam(required = false) List<Long> tagIds,
                             @RequestParam(required = false) String newTagName) {
        
        TaskDto createdTask = taskApiService.createTask(projectId, request);

        // Milestone handle
        if (newMilestoneName != null && !newMilestoneName.isBlank()) {
            MilestoneDto newMilestone = milestoneApiService.createMilestone(projectId, new MilestoneCreateRequest(newMilestoneName, newMilestoneStartDate, newMilestoneEndDate));
            milestoneId = newMilestone.milestoneId();
        }
        if (milestoneId != null) {
            taskApiService.setMilestone(projectId, createdTask.taskId(), new TaskMilestoneRequest(milestoneId));
        }

        // Tags handle
        List<Long> finalTagIds = tagIds != null ? new ArrayList<>(tagIds) : new ArrayList<>();
        if (newTagName != null && !newTagName.isBlank()) {
            TagDto newTag = tagApiService.createTag(projectId, new TagCreateRequest(newTagName));
            finalTagIds.add(newTag.tagId());
        }
        if (!finalTagIds.isEmpty()) {
            taskApiService.addTags(projectId, createdTask.taskId(), new TaskTagRequest(finalTagIds));
        }

        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{task-id}")
    public String getTask(Model model, @PathVariable("project-id") Long projectId, @PathVariable("task-id") Long taskId) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("task", taskApiService.getTask(projectId, taskId));
        model.addAttribute("project", projectApiService.getProjectDetail(projectId));
        model.addAttribute("tags", tagApiService.getTags(projectId));
        return "task-detail";
    }

    @PostMapping("/{task-id}/edit")
    public String updateTask(@PathVariable("project-id") Long projectId, @PathVariable("task-id") Long taskId, @Valid @ModelAttribute TaskUpdateRequest request) {
        taskApiService.updateTask(projectId, taskId, request);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{task-id}/delete")
    public String deleteTask(@PathVariable("project-id") Long projectId, @PathVariable("task-id") Long taskId) {
        taskApiService.deleteTask(projectId, taskId);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{task-id}/milestones")
    public String setMilestone(@PathVariable("project-id") Long projectId, 
                               @PathVariable("task-id") Long taskId, 
                               @RequestParam(required = false) Long milestoneId,
                               @RequestParam(required = false) String newMilestoneName,
                               @RequestParam(required = false) LocalDate newMilestoneStartDate,
                               @RequestParam(required = false) LocalDate newMilestoneEndDate) {
        
        if (newMilestoneName != null && !newMilestoneName.isBlank()) {
            MilestoneDto newMilestone = milestoneApiService.createMilestone(projectId, new MilestoneCreateRequest(newMilestoneName, newMilestoneStartDate, newMilestoneEndDate));
            milestoneId = newMilestone.milestoneId();
        }
        
        taskApiService.setMilestone(projectId, taskId, new TaskMilestoneRequest(milestoneId));
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }

    @PostMapping("/{task-id}/tags")
    public String addTags(@PathVariable("project-id") Long projectId, 
                          @PathVariable("task-id") Long taskId, 
                          @RequestParam(required = false) List<Long> tagIds,
                          @RequestParam(required = false) String newTagName) {
        
        List<Long> finalTagIds = tagIds != null ? new ArrayList<>(tagIds) : new ArrayList<>();
        if (newTagName != null && !newTagName.isBlank()) {
            TagDto newTag = tagApiService.createTag(projectId, new TagCreateRequest(newTagName));
            finalTagIds.add(newTag.tagId());
        }
        
        taskApiService.addTags(projectId, taskId, new TaskTagRequest(finalTagIds));
        return "redirect:/projects/" + projectId + "/tasks/" + taskId;
    }
}
