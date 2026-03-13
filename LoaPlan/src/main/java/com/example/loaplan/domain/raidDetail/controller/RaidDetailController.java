package com.example.loaplan.domain.raidDetail.controller;

import com.example.loaplan.domain.raidDetail.dto.RaidDetailDto;
import com.example.loaplan.domain.raidDetail.service.RaidDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/raid")
@RequiredArgsConstructor
public class RaidDetailController {

    private final RaidDetailService raidDetailService;

    @GetMapping("/detail")
    public List<RaidDetailDto> getRaidDetail(@RequestParam Long taskId) {
        return raidDetailService.getByTask(taskId);
    }
}
