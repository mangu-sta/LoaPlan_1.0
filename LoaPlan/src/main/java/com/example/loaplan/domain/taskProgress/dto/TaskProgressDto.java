package com.example.loaplan.domain.taskProgress.dto;

import com.example.loaplan.domain.raidDetail.dto.RaidDetailDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskProgressDto {
    private Long id;
    private Long taskId; // ⭐프론트가 필요로 함
    private String name; // task.name or customName
    private String type; // DAILY / WEEKLY / RAID / EVENT / CUSTOM
    private Integer maxPhase; // task.maxPhase or 1
    private Integer progressPhase;
    private Boolean isCompleted;
    private List<RaidDetailDto> raidDetails;
    private List<String> difficultyData;
    private String officialDays; // MON,TUE...
    private String resetType; // DAILY / WEEKLY (for custom)

}