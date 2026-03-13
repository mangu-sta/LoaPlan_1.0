package com.example.loaplan.domain.raidInfo.dto;


import com.example.loaplan.domain.raidDetail.entity.RaidDetailEntity;
import com.example.loaplan.domain.task.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RaidInfoDto {
    private Long taskId;
    private String name;
    private int maxPhase;
    private List<RaidDetailDto> details;

    public RaidInfoDto(TaskEntity task, List<RaidDetailEntity> details) {
        this.taskId = task.getId();
        this.name = task.getName();
        this.maxPhase = task.getMaxPhase();
        this.details = details.stream().map(RaidDetailDto::new).toList();

    }
}

