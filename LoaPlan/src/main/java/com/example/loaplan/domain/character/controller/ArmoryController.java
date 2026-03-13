package com.example.loaplan.domain.character.controller;

import com.example.loaplan.global.api.dto.CharacterArmoryDto;
import com.example.loaplan.global.api.LostArkApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/armory")
public class ArmoryController {

    private final LostArkApiClient api;

    @GetMapping("/search")
    public ResponseEntity<?> searchCharacter(@RequestParam String nickname) {
        CharacterArmoryDto dto = api.getArmory(nickname);
        if (dto == null) {
            return ResponseEntity.status(404).body("캐릭터를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(dto);
    }
}
