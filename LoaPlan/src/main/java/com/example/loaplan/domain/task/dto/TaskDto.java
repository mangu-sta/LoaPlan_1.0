package com.example.loaplan.domain.task.dto;
import lombok.*;

import java.time.LocalDateTime;


@Getter @Setter
public class TaskDto {

    private Long id;
    private String name;
    private String type;
    private String officialDays;
    private Integer maxPhase;
    private Boolean isOfficial;
    private Boolean isActive;
}
