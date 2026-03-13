package com.example.loaplan.domain.raidDetail.entity;
import com.example.loaplan.domain.task.entity.TaskEntity;
import jakarta.persistence.*; import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "raid_detail")
@Getter @Setter
public class RaidDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    private Integer phaseNumber;

    private Integer goldReward;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private Boolean isBiweekly;
}
