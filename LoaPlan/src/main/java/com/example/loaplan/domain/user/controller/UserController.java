package com.example.loaplan.domain.user.controller;
import com.example.loaplan.domain.character.dto.CharacterDto;
import com.example.loaplan.domain.character.entity.CharacterEntity;
import com.example.loaplan.domain.character.repository.CharacterRepository;
import com.example.loaplan.domain.character.service.CharacterService;
import com.example.loaplan.domain.user.entity.UserEntity;
import com.example.loaplan.domain.user.repository.UserRepository;
import com.example.loaplan.domain.user.service.UserService;
import com.example.loaplan.global.login.dto.JoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CharacterService characterService;

    // 이메일 중복 체크
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean available = userService.checkEmail(email);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 닉네임 중복 체크
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean available = userService.checkNickname(nickname);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 회원가입 처리
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinDTO dto) {
        userService.join(dto);
        return ResponseEntity.ok("회원가입 성공");
    }




    @GetMapping("/me")
    public String me() {
        return "현재 로그인된 유저 정보 (추후 SecurityContext로 확장)";
    }




    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String nickname) {

        String imageUrl = characterService.getProfileImageByNickname(nickname);

        return ResponseEntity.ok(
                Map.of("characterImage", imageUrl == null ? "" : imageUrl)
        );
    }








}