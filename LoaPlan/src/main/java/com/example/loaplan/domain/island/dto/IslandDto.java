package com.example.loaplan.domain.island.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IslandDto {
    private String name;           // 섬 이름
    private String location;       // 위치
    private String nextTime;       // 다음 등장 시간
    private List<String> startTimes; // 오늘의 모든 시간대
    private boolean gold;          // 골드 여부
    private List<String> rewards;  // ✅ 보상 목록 (추가)
}