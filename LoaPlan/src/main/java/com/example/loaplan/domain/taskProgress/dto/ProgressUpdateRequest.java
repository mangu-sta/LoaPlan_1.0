package com.example.loaplan.domain.taskProgress.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProgressUpdateRequest {
    private int progressPhase;
    private List<String> difficultyData;  // NORMAL / HARD / NONE
}

