package com.example.loaplan.domain.task.repository;

import com.example.loaplan.domain.task.entity.TaskEntity;
import com.example.loaplan.domain.task.entity.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByIsActiveTrueOrderByTypeAsc();
    List<TaskEntity> findByType(TaskType type);


}
