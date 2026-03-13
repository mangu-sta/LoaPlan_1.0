package com.example.loaplan.domain.raidDetail.dto;
import com.example.loaplan.domain.raidDetail.entity.RaidDetailEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
public class RaidDetailDto {

    private Long id;
    private Long taskId;
    private Integer phaseNumber;
    private Integer goldReward;
    private String difficulty;
    private Boolean isBiweekly;

    // ⭐ Entity → DTO 변환 메서드 추가
    public static RaidDetailDto from(RaidDetailEntity e) {
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
