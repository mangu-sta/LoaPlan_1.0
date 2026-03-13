package com.example.loaplan.global.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final Map<String, String> codeStorage = new HashMap<>(); // 임시 저장 (실서비스면 Redis 추천)

    public String  sendVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        codeStorage.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[LoaPlan] 이메일 인증 코드");
        message.setText("인증 코드는 " + code + " 입니다. 3분 내에 입력해주세요.");

        mailSender.send(message);

        return code;
    }

    public boolean verifyCode(String email, String inputCode) {
        String savedCode = codeStorage.get(email);
        return savedCode != null && savedCode.equals(inputCode);
    }
}
