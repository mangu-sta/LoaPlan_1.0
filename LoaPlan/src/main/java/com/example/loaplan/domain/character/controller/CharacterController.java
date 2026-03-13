package com.example.loaplan.domain.character.controller;

import com.example.loaplan.domain.character.dto.CharacterDto;
import com.example.loaplan.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping
    public ResponseEntity<CharacterDto> addCharacter(@RequestBody CharacterDto req, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build(); // 또는 403
        }

        String nickname = req.getNickname();
        if (nickname == null || nickname.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String email = principal.getName();
        CharacterDto dto = characterService.addCharacter(nickname, email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<CharacterDto>> getMyCharacters(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String email = principal.getName();
        List<CharacterDto> list = characterService.getCharactersByUser(email);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/reorder")
    public ResponseEntity<?> reorderCharacters(@RequestBody List<Long> orderedIds, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String email = principal.getName();
        characterService.updateOrder(email, orderedIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{characterId}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long characterId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String email = principal.getName();
        try {
            characterService.deleteCharacter(characterId, email);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<List<CharacterDto>> refreshCharacters(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String email = principal.getName();
        List<CharacterDto> list = characterService.refreshCharacters(email);
        return ResponseEntity.ok(list);
    }

}
