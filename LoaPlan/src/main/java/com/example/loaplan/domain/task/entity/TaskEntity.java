package com.example.loaplan.domain.task.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "task")
@Getter @Setter
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TaskType type; // DAILY / WEEKLY / RAID / EVENT

    private Integer maxPhase;

    private String officialDays;

    private Boolean isOfficial;
    private Boolean isActive;

    private LocalDateTime createdAt;
}

