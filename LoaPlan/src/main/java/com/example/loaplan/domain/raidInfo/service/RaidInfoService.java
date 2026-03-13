package com.example.loaplan.domain.raidInfo.service;


import com.example.loaplan.domain.raidDetail.entity.RaidDetailEntity;
import com.example.loaplan.domain.raidDetail.repository.RaidDetailRepository;
import com.example.loaplan.domain.raidInfo.dto.RaidInfoDto;
import com.example.loaplan.domain.task.entity.TaskEntity;
import com.example.loaplan.domain.task.entity.TaskType;
import com.example.loaplan.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RaidInfoService {

    private final TaskRepository taskRepository;
    private final RaidDetailRepository raidDetailRepository;

    public List<RaidInfoDto> getRaidInfos() {
        List<TaskEntity> raids = taskRepository.findByType(TaskType.RAID);

        return raids.stream()
                .map(task -> {
                    List<RaidDetailEntity> details =
                            raidDetailRepository.findByTaskIdOrderByPhaseNumberAsc(task.getId());
                    return new RaidInfoDto(task, details);
                })
                .toList();
    }

}
