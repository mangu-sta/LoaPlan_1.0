package com.example.loaplan.domain.taskProgress.controller;

import com.example.loaplan.domain.taskProgress.dto.ProgressUpdateRequest;
import com.example.loaplan.domain.taskProgress.dto.TaskProgressDto;
import com.example.loaplan.domain.taskProgress.entity.ResetType;
import com.example.loaplan.domain.taskProgress.entity.TaskProgressEntity;
import com.example.loaplan.domain.taskProgress.service.TaskProgressService;
import com.example.loaplan.domain.taskProgress.entity.ResetType;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class TaskProgressController {

    private final TaskProgressService service;

    @GetMapping("/list")
    public List<TaskProgressDto> getList(@RequestParam Long characterId) {
        return service.getScheduleList(characterId);
    }


    @PostMapping("/add/official")
    public TaskProgressDto  addOfficial(
            @RequestParam Long characterId,
            @RequestParam Long taskId
    ) {
        return service.addOfficialSchedule(characterId, taskId);
    }

    @PostMapping("/add/custom")
    public TaskProgressDto  addCustom(
            @RequestParam Long characterId,
            @RequestParam String name,
            @RequestParam ResetType resetType
    ) {
        return service.addCustomSchedule(characterId, name, resetType);
    }

    @PostMapping("/update")
    public void update(
            @RequestParam Long id,
            @RequestBody ProgressUpdateRequest req
    ) {
        service.updateProgress(id, req);
    }



    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        service.removeProgress(id);
    }
}
