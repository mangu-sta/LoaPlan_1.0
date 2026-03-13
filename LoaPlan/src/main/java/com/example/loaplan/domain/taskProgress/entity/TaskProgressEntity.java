package com.example.loaplan.domain.taskProgress.entity;

import com.example.loaplan.domain.character.entity.CharacterEntity;
import com.example.loaplan.domain.task.entity.TaskEntity;
import com.example.loaplan.domain.taskProgress.dto.TaskProgressDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_progress")
@Getter
@Setter
public class TaskProgressEntity {   // ← 반드시 이렇게!!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private CharacterEntity character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskEntity task;   // 커스텀일 경우 NULL

    private String customName; // 커스텀 스케줄명
    @Enumerated(EnumType.STRING)
    private ResetType resetType; // DAILY / WEEKLY

    private int progressPhase; // 진행 단계
    private boolean isCompleted;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Column(name = "difficulty_data")
    private String difficultyData;


    public boolean isCustom() {
        return task == null;
    }





}
