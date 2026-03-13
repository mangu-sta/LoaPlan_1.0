package com.example.loaplan.domain.raidInfo.dto;


import com.example.loaplan.domain.raidDetail.entity.RaidDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RaidDetailDto {
    private int phase;
    private int gold;
    private String difficulty;

    public RaidDetailDto(RaidDetailEntity e) {
        this.phase = e.getPhaseNumber();
        this.gold = e.getGoldReward();
        this.difficulty = e.getDifficulty().name();
    }
}

