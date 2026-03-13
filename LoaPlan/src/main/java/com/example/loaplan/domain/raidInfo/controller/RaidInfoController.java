package com.example.loaplan.domain.raidInfo.controller;

import com.example.loaplan.domain.raidInfo.service.RaidInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RaidInfoController {

    private final RaidInfoService raidService;

    @GetMapping("/raid/list")
    public ResponseEntity<?> getRaidList() {
        return ResponseEntity.ok(raidService.getRaidInfos());
    }
}

