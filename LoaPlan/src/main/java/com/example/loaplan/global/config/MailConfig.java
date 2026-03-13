package com.example.loaplan.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // ✅ 실제 SMTP 대신 콘솔로 출력만 하는 Mock 객체
        return new JavaMailSenderImpl() {
            @Override
            public void send(SimpleMailMessage message) {
                System.out.println("=========================================");
                System.out.println("📩 [테스트용 가짜메일] 인증코드 메일 전송됨!");
                System.out.println("수신자: " + String.join(",", message.getTo()));
                System.out.println("제목: " + message.getSubject());
                System.out.println("내용: " + message.getText());
                System.out.println("=========================================");
            }
        };
    }
}
