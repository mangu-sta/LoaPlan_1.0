package com.example.loaplan.domain.user.service;


import com.example.loaplan.domain.user.entity.UserEntity;
import com.example.loaplan.domain.user.repository.UserRepository;
import com.example.loaplan.global.login.dto.JoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 이메일 중복 확인
    public boolean checkEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    // 닉네임 중복 확인
    public boolean checkNickname(String nickname) {
        return userRepository.findByNickname(nickname).isEmpty();
    }

    // 비밀번호 정규식 검사 (특수문자 포함)
    private boolean validatePassword(String pw) {
        return pw.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,20}$");
    }

    // 회원가입
    public void join(JoinDTO dto) {

        if (!checkEmail(dto.getEmail()))
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");

        if (!checkNickname(dto.getNickname()))
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        if (!validatePassword(dto.getPassword()))
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");

        UserEntity user = UserEntity.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        userRepository.save(user);
    }


    public void loginSuccess(UserDetails userDetails) {
        // 인증 정보 생성
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        // ✅ SecurityContext에 등록 → 세션 자동 생성됨
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }






}