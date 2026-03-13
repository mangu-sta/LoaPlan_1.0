package com.example.loaplan.domain.raidDetail.repository;

import com.example.loaplan.domain.raidDetail.entity.RaidDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaidDetailRepository extends JpaRepository<RaidDetailEntity, Long> {

    List<RaidDetailEntity> findByTaskIdOrderByPhaseNumberAsc(Long taskId);

    List<RaidDetailEntity> findByTaskId(Long taskId);

}
