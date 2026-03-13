package com.example.loaplan.domain.island.controller;

import com.example.loaplan.domain.island.dto.IslandDto;
import com.example.loaplan.domain.island.service.IslandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/islands")
@RequiredArgsConstructor
public class IslandApiController {

    private final IslandService islandService;

    @GetMapping("/today")
    public Map<String, Object> getTodayIslands() {
        List<IslandDto> adventureIslands = islandService.getTodayAdventureIslands();

        Map<String, Object> res = new HashMap<>();
        res.put("adventureIslands", adventureIslands);
        res.put("nowEpochMs", ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli());
        res.put("zoneOffset", "+09:00");
        return res;
    }
}
