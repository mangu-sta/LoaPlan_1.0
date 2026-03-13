package com.example.loaplan.domain.task.service;

import com.example.loaplan.domain.task.dto.TaskDto;
import com.example.loaplan.domain.task.entity.TaskEntity;
import com.example.loaplan.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskDto> getAllActiveTasks() {
        return taskRepository.findByIsActiveTrueOrderByTypeAsc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private TaskDto toDto(TaskEntity t) {
        TaskDto dto = new TaskDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setType(t.getType().name());
        dto.setOfficialDays(t.getOfficialDays());
        dto.setMaxPhase(t.getMaxPhase());
        dto.setIsOfficial(t.getIsOfficial());
        dto.setIsActive(t.getIsActive());
        return dto;
    }
}
