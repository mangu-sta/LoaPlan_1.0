package com.example.loaplan.global.login.controller;

import com.example.loaplan.global.login.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        String code = emailService.sendVerificationCode(email);

        return ResponseEntity.ok(Map.of("message", "이메일 발송 완료","tempCode", code));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        boolean valid = emailService.verifyCode(email, code);

        if (valid) {
            return ResponseEntity.ok(Map.of("verified", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of("verified", false));
        }
    }
}
