package com.example.loaplan.domain.raidDetail.service;

import com.example.loaplan.domain.raidDetail.dto.RaidDetailDto;
import com.example.loaplan.domain.raidDetail.entity.RaidDetailEntity;
import com.example.loaplan.domain.raidDetail.repository.RaidDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RaidDetailService {

    private final RaidDetailRepository raidDetailRepository;

    public List<RaidDetailDto> getByTask(Long taskId) {
        return raidDetailRepository.findByTaskIdOrderByPhaseNumberAsc(taskId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private RaidDetailDto toDto(RaidDetailEntity e) {
        RaidDetailDto dto = new RaidDetailDto();
        dto.setId(e.getId());
        dto.setTaskId(e.getTask().getId());
        dto.setPhaseNumber(e.getPhaseNumber());
        dto.setGoldReward(e.getGoldReward());
        dto.setDifficulty(e.getDifficulty().name());
        dto.setIsBiweekly(e.getIsBiweekly());
        return dto;
    }
}
