package com.example.loaplan.domain.taskProgress.service;

import com.example.loaplan.domain.character.entity.CharacterEntity;
import com.example.loaplan.domain.character.repository.CharacterRepository;
import com.example.loaplan.domain.raidDetail.dto.RaidDetailDto;
import com.example.loaplan.domain.raidDetail.repository.RaidDetailRepository;
import com.example.loaplan.domain.task.entity.TaskEntity;
import com.example.loaplan.domain.task.entity.TaskType;
import com.example.loaplan.domain.task.repository.TaskRepository;
import com.example.loaplan.domain.taskProgress.dto.ProgressUpdateRequest;
import com.example.loaplan.domain.taskProgress.dto.TaskProgressDto;
import com.example.loaplan.domain.taskProgress.entity.TaskProgressEntity;
import com.example.loaplan.domain.taskProgress.repository.TaskProgressRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.loaplan.domain.taskProgress.entity.ResetType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskProgressService {

    private final TaskProgressRepository taskProgressRepository;
    private final TaskRepository taskRepository;
    private final CharacterRepository characterRepository;
    private final RaidDetailRepository raidDetailRepository;

    /* 캐릭터의 모든 스케줄 진행상태 조회 */
    public List<TaskProgressDto> getScheduleList(Long characterId) {
        return taskProgressRepository.findByCharacterId(characterId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /* 공식 스케줄 추가 */
    public TaskProgressDto addOfficialSchedule(Long characterId, Long taskId) {
        CharacterEntity character = characterRepository.findById(characterId).orElseThrow();
        TaskEntity task = taskRepository.findById(taskId).orElseThrow();

        TaskProgressEntity tp = new TaskProgressEntity();
        tp.setCharacter(character);
        tp.setTask(task);
        tp.setProgressPhase(0);
        tp.setCompleted(false);
        TaskProgressEntity saved = taskProgressRepository.save(tp);

        return toDto(saved);
    }

    /* 커스텀 스케줄 추가 */
    public TaskProgressDto addCustomSchedule(Long characterId, String name, ResetType resetType) {
        CharacterEntity character = characterRepository.findById(characterId).orElseThrow();

        TaskProgressEntity tp = new TaskProgressEntity();
        tp.setCharacter(character);
        tp.setTask(null);
        tp.setCustomName(name);
        tp.setResetType(resetType);
        tp.setProgressPhase(0);
        tp.setCompleted(false);
        TaskProgressEntity saved = taskProgressRepository.save(tp);

        return toDto(saved);
    }

    /* 진행도 업데이트 */
    public void updateProgress(Long id, ProgressUpdateRequest req) {

        TaskProgressEntity tp = taskProgressRepository.findById(id)
                .orElseThrow();

        tp.setProgressPhase(req.getProgressPhase());

        try {
            ObjectMapper mapper = new ObjectMapper();
            // ✅ 데이터 검증 및 정제 (유효한 값만 허용)
            List<String> rawData = req.getDifficultyData();
            if (rawData != null) {
                List<String> sanitizedParams = rawData.stream()
                        .map(d -> {
                            if ("NORMAL".equals(d) || "HARD".equals(d))
                                return d;
                            return "NONE"; // 이상한 값은 NONE 처리
                        })
                        .toList();
                tp.setDifficultyData(mapper.writeValueAsString(sanitizedParams));
            } else {
                tp.setDifficultyData("[]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 기존 completed 계산 로직 그대로
        boolean isCompleted = false;
        if (tp.getTask() != null) {
            int maxPhase = tp.getTask().getMaxPhase();
            isCompleted = req.getProgressPhase() >= maxPhase;
        } else {
            isCompleted = req.getProgressPhase() >= 1;
        }

        tp.setCompleted(isCompleted);

        taskProgressRepository.save(tp);
    }

    /* 삭제 */
    public void removeProgress(Long progressId) {
        taskProgressRepository.deleteById(progressId);
    }

    private TaskProgressDto toDto(TaskProgressEntity e) {
        TaskProgressDto dto = new TaskProgressDto();
        dto.setId(e.getId());
        dto.setProgressPhase(e.getProgressPhase());
        dto.setIsCompleted(e.isCompleted());

        if (e.getTask() != null) {
            dto.setTaskId(e.getTask().getId());
            dto.setName(e.getTask().getName());
            dto.setType(e.getTask().getType().name());
            dto.setMaxPhase(e.getTask().getMaxPhase());
            dto.setOfficialDays(e.getTask().getOfficialDays()); // ✅ 요일 정보 매핑

            // 🔥 ResetType 매핑 (프론트 분류용)
            if (e.getTask().getType() == TaskType.DAILY || e.getTask().getType() == TaskType.EVENT) {
                dto.setResetType("DAILY");
            } else {
                dto.setResetType("WEEKLY");
            }

            // 🔥 레이드 정보 추가
            if (e.getTask().getType() == TaskType.RAID) {
                List<RaidDetailDto> details = raidDetailRepository
                        .findByTaskId(e.getTask().getId())
                        .stream()
                        .map(RaidDetailDto::from)
                        .toList();

                dto.setRaidDetails(details);
            }
        } else {
            dto.setTaskId(null);
            dto.setName(e.getCustomName());
            dto.setType("CUSTOM");
            dto.setMaxPhase(1);
            if (e.getResetType() != null) {
                dto.setResetType(e.getResetType().name());
            } else {
                dto.setResetType("DAILY"); // 기본값 방어
            }
        }

        if (e.getDifficultyData() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<String> arr = mapper.readValue(e.getDifficultyData(), new TypeReference<List<String>>() {
                });
                dto.setDifficultyData(arr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return dto;
    }

}
