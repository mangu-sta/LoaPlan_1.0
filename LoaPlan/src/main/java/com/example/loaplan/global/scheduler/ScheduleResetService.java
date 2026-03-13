package com.example.loaplan.global.scheduler;

import com.example.loaplan.domain.task.entity.TaskType;
import com.example.loaplan.domain.taskProgress.entity.ResetType;
import com.example.loaplan.domain.taskProgress.repository.TaskProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleResetService {

    private final TaskProgressRepository taskProgressRepository;

    // 매일 오전 6시 실행
    @Transactional
    @Scheduled(cron = "0 0 6 * * *")
    public void runDailyReset() {
        log.info("[ScheduleReset] Daily Reset Started at {}", LocalDateTime.now());

        // 1. TaskType.DAILY, EVENT (Official)
        // 2. Custom (ResetType.DAILY)
        List<TaskType> dailyTypes = Arrays.asList(
                TaskType.DAILY,
                TaskType.EVENT);

        taskProgressRepository.resetDaily(dailyTypes, ResetType.DAILY);

        log.info("[ScheduleReset] Daily Reset Completed.");
    }

    // 매주 수요일 오전 6시 실행
    @Transactional
    @Scheduled(cron = "0 0 6 * * WED")
    public void runWeeklyReset() {
        log.info("[ScheduleReset] Weekly Reset Started at {}", LocalDateTime.now());

        // 1. TaskType.WEEKLY, RAID (Official)
        // 2. Custom (ResetType.WEEKLY)
        List<TaskType> weeklyTypes = Arrays.asList(
                TaskType.WEEKLY,
                TaskType.RAID);

        taskProgressRepository.resetWeekly(weeklyTypes, ResetType.WEEKLY);

        log.info("[ScheduleReset] Weekly Reset Completed.");
    }
}
