package com.example.loaplan.domain.task.controller;

import com.example.loaplan.domain.task.dto.TaskDto;
import com.example.loaplan.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/list")
    public List<TaskDto> list() {
        return taskService.getAllActiveTasks();
    }
}
