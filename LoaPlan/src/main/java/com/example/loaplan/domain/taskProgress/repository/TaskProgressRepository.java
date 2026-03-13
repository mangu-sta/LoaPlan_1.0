package com.example.loaplan.domain.taskProgress.repository;

import com.example.loaplan.domain.taskProgress.entity.TaskProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskProgressRepository extends JpaRepository<TaskProgressEntity, Long> {

        List<TaskProgressEntity> findByCharacterId(Long characterId);

        @org.springframework.data.jpa.repository.Modifying
        @org.springframework.data.jpa.repository.Query("UPDATE TaskProgressEntity tp SET tp.progressPhase = 0, tp.isCompleted = false, tp.difficultyData = null "
                        +
                        "WHERE tp.task.type IN :dailyTypes OR (tp.task IS NULL AND tp.resetType = :customDaily)")
        void resetDaily(@org.springframework.data.repository.query.Param("dailyTypes") java.util.List<com.example.loaplan.domain.task.entity.TaskType> dailyTypes,
                        @org.springframework.data.repository.query.Param("customDaily") com.example.loaplan.domain.taskProgress.entity.ResetType customDaily);

        @org.springframework.data.jpa.repository.Modifying
        @org.springframework.data.jpa.repository.Query("UPDATE TaskProgressEntity tp SET tp.progressPhase = 0, tp.isCompleted = false, tp.difficultyData = null "
                        +
                        "WHERE tp.task.type IN :weeklyTypes OR (tp.task IS NULL AND tp.resetType = :customWeekly)")
        void resetWeekly(
                        @org.springframework.data.repository.query.Param("weeklyTypes") java.util.List<com.example.loaplan.domain.task.entity.TaskType> weeklyTypes,
                        @org.springframework.data.repository.query.Param("customWeekly") com.example.loaplan.domain.taskProgress.entity.ResetType customWeekly);
}
